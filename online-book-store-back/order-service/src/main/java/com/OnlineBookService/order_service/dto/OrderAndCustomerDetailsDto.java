package com.OnlineBookService.order_service.dto;

import com.OnlineBookService.order_service.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderAndCustomerDetailsDto {
    private ResponseProfileDto customerDetailsDto;
    private List<BookQuantityRequestDto> bookQuantityRequestDtoList;
}
