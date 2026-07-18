package com.OnlineBookService.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookOrderRequestDto {
    public String customerId;
    List<BookQuantityRequestDto> bookQuantityRequestDtoList;
}
