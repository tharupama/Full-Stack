package com.OnlineBookStore.AuthService.service;

import com.OnlineBookStore.AuthService.dto.LoginRequest;
import com.OnlineBookStore.AuthService.dto.LoginResponse;
import com.OnlineBookStore.AuthService.entity.User;
import com.OnlineBookStore.AuthService.repository.UserRepo;
import com.OnlineBookStore.AuthService.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    // Use ObjectProvider for lazy injection to break circular dependency
    @Autowired
    private ObjectProvider<AuthenticationManager> authenticationManagerProvider;

    @Autowired
    private JwtUtil  jwtUtil;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthority(user)
        );
    }
    //@Transactional
    public Set<SimpleGrantedAuthority> getAuthority(User user){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.getRole() != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));

        }
        return authorities;
    }

    public LoginResponse createJwtToken(LoginRequest loginRequest) throws Exception {
        String userName = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        authenticate(userName, password);

        User user = userRepo.findById(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthority(user)
        );

        String newGeneratedToken = jwtUtil.generateToken(userDetails);
        return new LoginResponse(newGeneratedToken, user);
    }


    private void authenticate(String userName, String password) throws Exception{
        try {
            authenticationManagerProvider.getObject().authenticate(new UsernamePasswordAuthenticationToken(userName,password));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
