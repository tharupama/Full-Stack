package com.OnlineBookService.order_service.service.impl;

import com.OnlineBookService.order_service.dto.BookOrderRequestDto;
import com.OnlineBookService.order_service.dto.BookQuantityRequestDto;
import com.OnlineBookService.order_service.entity.Order;
import com.OnlineBookService.order_service.entity.OrderItem;
import com.OnlineBookService.order_service.repository.OrderRepo;
import com.OnlineBookService.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private WebClient webClient;
    private OrderRepo orderRepo;
    private final BookServiceClient bookServiceClient;
    @Override


    public String saveOrder(BookOrderRequestDto bookOrderRequestDto) {
        System.out.println("inside save book order service");
        Order order = new Order();
        order.setCustomerId(bookOrderRequestDto.customerId);
        order.setOrderStatus("PENDING");

        for(BookQuantityRequestDto bookReq:bookOrderRequestDto.getBookQuantityRequestDtoList()){
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
}
