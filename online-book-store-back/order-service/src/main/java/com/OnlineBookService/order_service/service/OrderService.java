package com.OnlineBookService.order_service.service;

import com.OnlineBookService.order_service.dto.BookOrderRequestDto;

public interface OrderService {
    String saveOrder(BookOrderRequestDto bookOrderRequestDto);

    String updateOrderStatus(Long orderId, String status);

    String deleteOrder(Long id);
}
