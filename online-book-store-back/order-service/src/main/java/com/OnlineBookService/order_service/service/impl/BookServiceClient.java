package com.OnlineBookService.order_service.service.impl;


import com.OnlineBookService.order_service.entity.Notification;
import com.OnlineBookService.order_service.enums.Enums;
import com.OnlineBookService.order_service.repository.NotificationRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class BookServiceClient {
    private NotificationRepo notificationRepo;
    private final WebClient webClient;
    @Retry(name = "${spring.application.name}", fallbackMethod = "fallbackDeductQuantity")
    //@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "fallbackDeductQuantity")
    public boolean deductQuantityFromInventory(Long bookId, int quantity) {
        System.out.println("🔄 Attempting to deduct quantity for bookId: " + bookId);

        Boolean isUpdated = webClient.put()
                .uri("http://localhost:8081/api/v1/book-controller/update-quantity?bookId={bookId}&quantity={quantity}", bookId, quantity)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        return Boolean.TRUE.equals(isUpdated);
    }

    public boolean fallbackDeductQuantity(Long bookId, int quantity, Throwable t) {
        System.err.println("⚠️ Circuit Breaker/Retry triggered for bookId: " + bookId + "! Book Service is down. Error: " + t.getMessage());
        Notification notification = new Notification();
        notification.setMessage("Automatic book Quantity reduction during order placement !, Admin have to deduct the quantity manually for bookId: " + bookId + " and quantity: " + quantity);
        notification.setStatus(Enums.NOT_PERFORMED);
        notificationRepo.save(notification);
        return false;
    }
}