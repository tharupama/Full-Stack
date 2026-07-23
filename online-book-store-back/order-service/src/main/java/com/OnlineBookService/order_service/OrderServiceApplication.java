package com.OnlineBookService.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
