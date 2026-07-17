package com.online_book_store.book_service.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class MessageController {
    @Value("${spring.boot.message}")//in application properties(now on git)
    private String message;

    @GetMapping("/message")
    public String message() {
        return message;
    }
}
