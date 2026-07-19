package com.OnlineBookService.order_service.service;

import com.OnlineBookService.order_service.entity.Notification;
import com.OnlineBookService.order_service.enums.Enums;

public interface NotificationService {
    Notification getNotification();

    String updateNotification(Long notificationId, Enums status);

    String deleteNotification(Long notificationId);
}
