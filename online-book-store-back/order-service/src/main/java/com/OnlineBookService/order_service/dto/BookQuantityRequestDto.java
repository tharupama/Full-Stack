package com.OnlineBookService.order_service.dto;

import lombok.Data;

@Data
public class BookQuantityRequestDto {
    private Long id;
    private String title;
    private double price;
    private int quantity;
}
