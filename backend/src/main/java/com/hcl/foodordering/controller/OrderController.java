package com.hcl.foodordering.controller;

import com.hcl.foodordering.dto.order.OrderResponse;
import com.hcl.foodordering.dto.order.CancelOrderRequest;
import com.hcl.foodordering.dto.order.PlaceOrderRequest;
import com.hcl.foodordering.service.order.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        log.debug("Placing order (mocked checkout).");
        return ResponseEntity.ok(orderService.placeOrder(request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> myOrders() {
        return ResponseEntity.ok(orderService.listMyOrders());
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody(required = false) CancelOrderRequest request
    ) {
        CancelOrderRequest safeRequest = request == null ? new CancelOrderRequest(null) : request;
        return ResponseEntity.ok(orderService.cancelOrder(orderId, safeRequest));
    }
}

