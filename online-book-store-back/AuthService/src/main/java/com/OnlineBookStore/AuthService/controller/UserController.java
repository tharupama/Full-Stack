package com.OnlineBookStore.AuthService.controller;

import com.OnlineBookStore.AuthService.dto.SignupRequest;
import com.OnlineBookStore.AuthService.entity.User;
import com.OnlineBookStore.AuthService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/init")
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }
    @PostMapping("/signup")
    public User signup(@RequestBody SignupRequest signupRequest) {
        return userService.registerNewUser(signupRequest);
    }
}
