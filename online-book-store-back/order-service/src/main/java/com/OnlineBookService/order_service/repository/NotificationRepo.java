package com.OnlineBookService.order_service.repository;

import com.OnlineBookService.order_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    Notification getAllNotifications();
}
