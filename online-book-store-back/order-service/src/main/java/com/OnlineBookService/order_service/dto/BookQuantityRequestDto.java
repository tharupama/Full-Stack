package com.OnlineBookService.order_service.dto;

import lombok.Data;

@Data
public class BookQuantityRequestDto {
    public Long bookId;
    public int quantity;
}
