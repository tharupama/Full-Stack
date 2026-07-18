package com.OnlineBookService.order_service.controller;

import com.OnlineBookService.order_service.dto.BookOrderRequestDto;
import com.OnlineBookService.order_service.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order-controller")
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;
    @PostMapping("/save")
    public String orderSave(@RequestBody BookOrderRequestDto bookOrderRequestDto){
        String result = orderService.saveOrder(bookOrderRequestDto);
        return result;
    }
    @PutMapping("/updateStatus")
    public String orderUpdate(@RequestParam Long orderId, @RequestParam String status){
    String result = orderService.updateOrderStatus(orderId, status);
    return result;
    }
    @DeleteMapping("/delete/{id}")
    public String orderDelete(@PathVariable Long id){
        String result = orderService.deleteOrder(id);
        return result;
    }
}
