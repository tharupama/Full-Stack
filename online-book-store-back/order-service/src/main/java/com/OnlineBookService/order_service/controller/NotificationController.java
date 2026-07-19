package com.OnlineBookService.order_service.controller;

import com.OnlineBookService.order_service.entity.Notification;
import com.OnlineBookService.order_service.enums.Enums;
import com.OnlineBookService.order_service.service.NotificationService;
import com.OnlineBookService.order_service.utill.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notification-controller")
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class NotificationController {
    private NotificationService notificationService;

    @GetMapping("/getNotification")
    public List<Notification> getNotification(){
        return notificationService.getNotification();
    }

    @PutMapping("/updateNotification")
    public ResponseEntity<StandardResponse> updateNotification(@RequestParam Long notificationId, @RequestParam Enums status){
        String result =  notificationService.updateNotification(notificationId, status);
        return new ResponseEntity<StandardResponse>(new StandardResponse(200, "success", result), HttpStatus.OK);
    }

    @DeleteMapping("/deleteNotification/{notificationId}")
    public ResponseEntity<StandardResponse> deleteNotification(@PathVariable Long notificationId) {
        String result = notificationService.deleteNotification(notificationId);
        return new ResponseEntity<StandardResponse>(new StandardResponse(200, "success", result), HttpStatus.OK);
    }

}
