package com.hcl.foodordering.dto.order;

import com.hcl.foodordering.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long restaurantId,
        String deliveryAddress,
        String instructions,
        BigDecimal totalAmount,
        OrderStatus status,
        Instant createdAt,
        boolean emailSent,
        List<OrderItemResponse> items
) {
}

