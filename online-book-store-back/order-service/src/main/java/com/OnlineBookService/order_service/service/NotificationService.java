package com.OnlineBookService.order_service.service;

import com.OnlineBookService.order_service.entity.Notification;
import com.OnlineBookService.order_service.enums.Enums;

import java.util.List;

public interface NotificationService {
   List<Notification> getNotification();

    String updateNotification(Long notificationId, Enums status);

    String deleteNotification(Long notificationId);
}
