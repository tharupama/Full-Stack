package com.OnlineBookService.order_service.service;

import com.OnlineBookService.order_service.dto.BookQuantityRequestDto;

import java.util.List;

public interface OrderService {
    String saveOrder(String email, List<BookQuantityRequestDto> bookQuantityRequestDto);

    String updateOrderStatus(Long orderId, String status);

    String deleteOrder(Long id);
}
