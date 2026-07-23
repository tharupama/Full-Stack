package com.OnlineBookService.order_service.dto;

import com.OnlineBookService.order_service.entity.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponseDto {
    private Long orderId;
    private String customerId;
    private String orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
