package com.OnlineBookService.order_service.repository;

import com.OnlineBookService.order_service.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {


    List<OrderItem> findOrderItemByOrder_OrderId(Long orderOrderId);
}
