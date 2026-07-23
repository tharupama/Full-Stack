package com.OnlineBookService.order_service.service;

import com.OnlineBookService.order_service.dto.ResponseProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url="http://localhost:8080",value="AuthService")
public interface AuthServiceApiClient {
    @GetMapping("/get-user-by-email/{email}")
    ResponseProfileDto getUserByEmail(@PathVariable String email);
    }
