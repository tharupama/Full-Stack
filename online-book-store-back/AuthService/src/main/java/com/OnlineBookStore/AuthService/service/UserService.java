package com.OnlineBookStore.AuthService.service;

import com.OnlineBookStore.AuthService.dto.ResponseProfileDto;
import com.OnlineBookStore.AuthService.dto.SaveMoreDetailsDto;
import com.OnlineBookStore.AuthService.dto.SignupRequest;
import com.OnlineBookStore.AuthService.entity.User;
import com.OnlineBookStore.AuthService.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo  userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(SignupRequest signupRequest) {
        if(!userRepo.existsById(signupRequest.getEmail())) {
            User user = new User();
            user.setEmail(signupRequest.getEmail());
            user.setUsername(signupRequest.getUsername());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setRole("User");
            return userRepo.save(user);
            //return userRepo.save(new User(signupRequest.getEmail(),signupRequest.getUsername(),passwordEncoder.encode(signupRequest.getPassword()) ,"User"));
        }
        return null;
    }

    public void initRoleAndUser(){
        try{
            if(!userRepo.existsById("admin@gmail.com")){
                User user = new User();
                user.setEmail("admin@gmail.com");
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRole("Admin");
                userRepo.save(user);
            }
            if(!userRepo.existsById("user@gmail.com")){
                User user = new User();
                user.setEmail("user@gmail.com");
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("User");
                userRepo.save(user);
            }
        }catch(Exception e){
            System.err.println("Error initializing roles and users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String saveMoreDetails(String email, SaveMoreDetailsDto saveMoreDetails) {
        User user = userRepo.findById(email).orElse(null);
        if (user != null) {
            user.setUsername(saveMoreDetails.getUsername());
            user.setAddress(saveMoreDetails.getAddress());
            user.setMobile(saveMoreDetails.getMobile());
            user.setPostalCode(saveMoreDetails.getPostalCode());

            userRepo.save(user);
            return "User details updated successfully";
        }
        return "User not found";
    }

    public ResponseProfileDto getUserProfile(String email) {
        User user = userRepo.findById(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new ResponseProfileDto(
            user.getEmail(),
            user.getUsername(),
            user.getAddress(),
            user.getMobile(),
            user.getPostalCode()
        );
    }

    public ResponseProfileDto getUserByEmail(String email) {
        User user = userRepo.findById(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new ResponseProfileDto(
            user.getEmail(),
            user.getUsername(),
            user.getAddress(),
            user.getMobile(),
            user.getPostalCode()
        );
    }
}
