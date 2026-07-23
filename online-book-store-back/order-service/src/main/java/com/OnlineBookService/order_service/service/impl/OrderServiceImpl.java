package com.OnlineBookService.order_service.service.impl;

import com.OnlineBookService.order_service.dto.*;
import com.OnlineBookService.order_service.entity.Order;
import com.OnlineBookService.order_service.entity.OrderItem;
import com.OnlineBookService.order_service.repository.OrderItemRepo;
import com.OnlineBookService.order_service.repository.OrderRepo;
import com.OnlineBookService.order_service.service.AuthServiceApiClient;
import com.OnlineBookService.order_service.service.OrderService;
import com.OnlineBookService.order_service.utill.Mappers.OrderMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;



@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private AuthServiceApiClient authServiceApiClient;
    private OrderItemRepo orderItemRepo;
    private WebClient webClient;
    private OrderRepo orderRepo;
    private final BookServiceClient bookServiceClient;
    private OrderMapper orderMapper;
    @Override


    public String saveOrder(String email, List<BookQuantityRequestDto> bookQuantityRequestDto) {
        System.out.println("inside save book order service");
        Order order = new Order();
        order.setCustomerId(email);
        order.setOrderStatus("PENDING");

        for(BookQuantityRequestDto bookReq:bookQuantityRequestDto){
            OrderItem orderItem = new OrderItem();
            orderItem.setBookId(bookReq.getBookId());
            orderItem.setQuantity(bookReq.getQuantity());
            boolean isUpdated = bookServiceClient.deductQuantityFromInventory(bookReq.getBookId(), bookReq.getQuantity());
            if (!isUpdated) {
                System.out.println("Failed to deduct quantity for bookId: " + bookReq.getBookId() + ". Rolling back transaction.");
            }
            order.addItem(orderItem);
        }
        if(orderRepo.save(order) == null) {
            throw new RuntimeException("Failed to save order");
        }else{
            return "Order saved successfully";
        }
    }

    @Override
    public String updateOrderStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order != null) {
            order.setOrderStatus(status);
            orderRepo.save(order);
            return "Order status updated successfully";
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Override
    public String deleteOrder(Long id) {
     if(orderRepo.findById(id).isPresent()){
         orderRepo.deleteById(id);
         return "Order deleted successfully";
     }else{
         return "Order not found";
    }

    }

    @Override
    public OrderPaginatedResponseDto getAllOrders(int page, int size) {
        return new OrderPaginatedResponseDto(orderRepo.count(),orderMapper.orderPageToOrderResponseDtoList(orderRepo.findAll(PageRequest.of(page, size))));
    }

    @Override
    @CircuitBreaker(name = "order-service", fallbackMethod = "getCustomerDetailsFallback")
    public OrderAndCustomerDetailsDto getOrderAndCustomerDetailsDto(Long orderId,String email) {
        return new OrderAndCustomerDetailsDto(authServiceApiClient.getUserByEmail(email),orderMapper.orderItemsListToBookQuantittyRequestDtoList(orderItemRepo.findOrderItemByOrder_OrderId(orderId)));
    }
    public OrderAndCustomerDetailsDto getCustomerDetailsFallback(Long orderId, String email, Throwable t) {
        System.err.println("Error occurred while fetching customer details: " + t.getMessage());
        return new OrderAndCustomerDetailsDto(null,orderMapper.orderItemsListToBookQuantittyRequestDtoList(orderItemRepo.findOrderItemByOrder_OrderId(orderId)));
    }
}
