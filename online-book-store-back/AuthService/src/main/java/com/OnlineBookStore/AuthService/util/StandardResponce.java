package com.OnlineBookStore.AuthService.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StandardResponce {
    private int code;
    private String message;
    private Object data;
}
