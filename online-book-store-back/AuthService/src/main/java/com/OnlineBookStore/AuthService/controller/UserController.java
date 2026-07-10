package com.OnlineBookStore.AuthService.controller;

import com.OnlineBookStore.AuthService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/init")
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }
}
