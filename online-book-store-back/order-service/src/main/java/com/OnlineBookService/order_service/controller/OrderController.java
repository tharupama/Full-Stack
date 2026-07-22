package com.OnlineBookService.order_service.controller;

import com.OnlineBookService.order_service.dto.BookQuantityRequestDto;
import com.OnlineBookService.order_service.service.OrderService;
import com.OnlineBookService.order_service.utill.StandardResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.net.Webhook;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/order-controller") // ✅ Added leading slash

public class OrderController {

    private final OrderService orderService;

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    // ✅ Helper class to hold pending order data
    private record PendingOrder(String userEmail, List<BookQuantityRequestDto> cartItems) {}

    // ⚠️ TEMPORARY: In production, use Redis or a database table
    private static final Map<String, PendingOrder> pendingOrders = new ConcurrentHashMap<>();

    // ✅ Proper Constructor Injection
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = this.stripeSecretKey;
        System.out.println("✅ Stripe API Key initialized successfully.");
    }

//    @PostMapping("/save")
//    public ResponseEntity<StandardResponse> orderSave(
//            @RequestHeader("X-User-Name") String email,
//            @RequestBody List<BookQuantityRequestDto> bookQuantityRequestDto) {
//        System.out.println("email is "+email);
//        String result = orderService.saveOrder(email, bookQuantityRequestDto);
//        return ResponseEntity.ok(new StandardResponse(200, "Success", result));
//    }

    @PutMapping("/updateStatus")
    public ResponseEntity<StandardResponse> orderUpdate(
            @RequestParam Long orderId,
            @RequestParam String status) {
        String result = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(new StandardResponse(200, "Success", result));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse> orderDelete(@PathVariable Long id) {
        String result = orderService.deleteOrder(id);
        return ResponseEntity.ok(new StandardResponse(200, "Success", result));
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(
            @RequestHeader("X-User-Name") String userEmail, // ✅ Get email securely from API Gateway header
            @RequestBody Map<String, Object> data) {
        try {
            // ✅ DEBUG LOG 1: Prove the request reached the backend with the correct user
            System.out.println("📥 BACKEND: Received checkout request for user: " + userEmail);

            long amount = ((Number) data.get("amount")).longValue();
            // ❌ REMOVED: String userEmail = (String) data.get("userEmail");

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> rawCartItems = (List<Map<String, Object>>) data.get("cartItems");

            List<BookQuantityRequestDto> cartItems = rawCartItems.stream()
                    .map(item -> {
                        BookQuantityRequestDto dto = new BookQuantityRequestDto();
                        dto.setId(Long.valueOf(item.get("id").toString()));
                        dto.setTitle((String) item.get("title"));
                        dto.setPrice(((Number) item.get("price")).doubleValue());
                        dto.setQuantity(((Number) item.get("quantity")).intValue());
                        return dto;
                    })
                    .toList();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:4200/payment-success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:4200/payment-cancelled")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(amount)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Online Book Store Order")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            // ✅ DEBUG LOG 2: Prove the session was created
            System.out.println("✅ BACKEND: Created Stripe Session ID: " + session.getId());

            // ✅ Save pending order data (linked to Stripe session ID)
            pendingOrders.put(session.getId(), new PendingOrder(userEmail, cartItems));

            // ✅ DEBUG LOG 3: Prove it was saved to the map
            System.out.println("💾 BACKEND: Saved pending order for session: " + session.getId() + " with email: " + userEmail);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("url", session.getUrl());
            responseData.put("sessionId", session.getId());

            return ResponseEntity.ok(responseData);

        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // ✅ DEBUG LOG 4: Catch any other errors (like NullPointerException)
            System.err.println("❌ BACKEND ERROR in createCheckoutSession: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        System.out.println("🚨 WEBHOOK ENDPOINT HIT! 🚨");

        try {
            // Use fully qualified name to avoid import issues
            com.stripe.model.Event event = com.stripe.net.Webhook.constructEvent(payload, sigHeader, webhookSecret);
            System.out.println("📡 WEBHOOK: Event type is: '" + event.getType() + "'");

            if ("checkout.session.completed".equals(event.getType())) {
                System.out.println("✅ Event type matched 'checkout.session.completed'");

                com.stripe.model.EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                com.stripe.model.checkout.Session session = null;

                // ✅ THE FIX: Handle API version mismatches
                if (dataObjectDeserializer.getObject().isPresent()) {
                    session = (com.stripe.model.checkout.Session) dataObjectDeserializer.getObject().get();
                } else {
                    System.out.println("⚠️ Safe deserialization returned empty (API version mismatch). Trying unsafe...");
                    session = (com.stripe.model.checkout.Session) dataObjectDeserializer.deserializeUnsafe();
                }

                System.out.println("🔍 Session object is null? " + (session == null));

                if (session != null) {
                    String sessionId = session.getId();
                    System.out.println("🔍 WEBHOOK: Looking for session ID: " + sessionId);
                    System.out.println("🗺️ WEBHOOK: Current keys in pendingOrders map: " + pendingOrders.keySet());

                    PendingOrder pending = pendingOrders.remove(sessionId);

                    if (pending != null) {
                        System.out.println("✅ WEBHOOK: Found pending order! Saving to database...");
                        orderService.saveOrder(pending.userEmail(), pending.cartItems());
                        System.out.println("✅ WEBHOOK: Order created securely via webhook for: " + pending.userEmail());
                    } else {
                        System.out.println("⚠️ WEBHOOK: No pending order found for session ID: " + sessionId);
                    }
                }
            } else {
                System.out.println("⚠️ WEBHOOK: Ignored event type: " + event.getType());
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            System.err.println("❌ WEBHOOK ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error");
        }
    }
}