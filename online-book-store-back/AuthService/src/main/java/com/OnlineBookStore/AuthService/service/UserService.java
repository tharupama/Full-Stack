package com.OnlineBookStore.AuthService.service;

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
}
