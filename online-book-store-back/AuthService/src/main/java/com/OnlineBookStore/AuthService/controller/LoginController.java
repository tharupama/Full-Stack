package com.OnlineBookStore.AuthService.controller;

import com.OnlineBookStore.AuthService.dto.LoginRequest;
import com.OnlineBookStore.AuthService.dto.LoginResponse;
import com.OnlineBookStore.AuthService.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse createTokenAndLogin(@RequestBody LoginRequest loginRequest )throws Exception{
        System.out.println(loginRequest);
        return jwtService.createJwtToken(loginRequest);
    }
}
