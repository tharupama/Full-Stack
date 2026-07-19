package com.OnlineBookService.order_service.controller;

import com.OnlineBookService.order_service.entity.Notification;
import com.OnlineBookService.order_service.enums.Enums;
import com.OnlineBookService.order_service.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notification-controller")
@AllArgsConstructor
public class NotificationController {
    private NotificationService notificationService;

    @GetMapping("/getNotification")
    public Notification getNotification(){
        return notificationService.getNotification();
    }

    @PutMapping("/updateNotification")
    public String updateNotification(@RequestParam Long notificationId, @RequestParam Enums status){
        return notificationService.updateNotification(notificationId, status);
    }

    @DeleteMapping("/deleteNotification/{notificationId}")
    public String deleteNotification(@PathVariable Long notificationId) {
        return notificationService.deleteNotification(notificationId);
    }

}
