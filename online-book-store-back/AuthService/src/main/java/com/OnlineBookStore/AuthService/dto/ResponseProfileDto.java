package com.OnlineBookStore.AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseProfileDto {
    private String email;
    private String username;
    private String address;
    private String mobile;
    private String postalCode;
}
