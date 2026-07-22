package com.OnlineBookStore.AuthService.controller;

import com.OnlineBookStore.AuthService.dto.ResponseProfileDto;
import com.OnlineBookStore.AuthService.dto.SaveMoreDetailsDto;
import com.OnlineBookStore.AuthService.dto.SignupRequest;
import com.OnlineBookStore.AuthService.entity.User;
import com.OnlineBookStore.AuthService.service.UserService;
import com.OnlineBookStore.AuthService.util.StandardResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/save-more-details")
    public ResponseEntity<StandardResponce> saveMoreDetails(@RequestHeader("X-User-Name") String email, @RequestBody SaveMoreDetailsDto saveMoreDetails){
        System.out.println("Postal Code: " + saveMoreDetails.getPostalCode());
        String result = userService.saveMoreDetails(email,saveMoreDetails);
        return ResponseEntity.ok(new StandardResponce(200, "Success", result));
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseProfileDto> getUserProfile(@RequestHeader("X-User-Name") String email) {
        ResponseProfileDto user = userService.getUserProfile(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
