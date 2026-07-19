package com.OnlineBookService.order_service.service.impl;

import com.OnlineBookService.order_service.entity.Notification;
import com.OnlineBookService.order_service.enums.Enums;
import com.OnlineBookService.order_service.repository.NotificationRepo;
import com.OnlineBookService.order_service.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepo notificationRepo;
    @Override
    public Notification getNotification() {
        return notificationRepo.getAllNotifications();
    }

    @Override
    public String updateNotification(Long notificationId, Enums status) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setStatus(status);
            notificationRepo.save(notification);
            return "Notification updated successfully";
        }
        return "Notification not found";
    }

    @Override
    public String deleteNotification(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);
        if (notification != null) {
            notificationRepo.delete(notification);
            return "Notification deleted successfully";
        }
        return "Notification not found";
    }
}
