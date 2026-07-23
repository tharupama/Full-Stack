package com.OnlineBookService.order_service.service;

import com.OnlineBookService.order_service.dto.BookQuantityRequestDto;
import com.OnlineBookService.order_service.dto.OrderAndCustomerDetailsDto;
import com.OnlineBookService.order_service.dto.OrderPaginatedResponseDto;
import com.OnlineBookService.order_service.dto.OrderResponseDto;
import com.OnlineBookService.order_service.entity.Order;

import java.util.List;

public interface OrderService {
    String saveOrder(String email, List<BookQuantityRequestDto> bookQuantityRequestDto);

    String updateOrderStatus(Long orderId, String status);

    String deleteOrder(Long id);

    OrderPaginatedResponseDto getAllOrders(int page, int size);

    OrderAndCustomerDetailsDto getOrderAndCustomerDetailsDto(Long orderId, String email);
}
