package com.OnlineBookStore.AuthService.dto;

import com.OnlineBookStore.AuthService.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
    private String token;
    private User user;
}
