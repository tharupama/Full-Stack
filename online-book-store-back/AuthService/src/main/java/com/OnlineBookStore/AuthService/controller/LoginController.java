package com.OnlineBookStore.AuthService.controller;

import com.OnlineBookStore.AuthService.dto.LoginRequest;
import com.OnlineBookStore.AuthService.dto.LoginResponse;
import com.OnlineBookStore.AuthService.service.JwtService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")

public class LoginController {
    @Autowired
    private JwtService jwtService;
    //@Observed(name = "auth.login")
    @PostMapping("/login")
    public LoginResponse createTokenAndLogin(@RequestBody LoginRequest loginRequest )throws Exception{
        System.out.println(loginRequest);
        return jwtService.createJwtToken(loginRequest);
    }
}
