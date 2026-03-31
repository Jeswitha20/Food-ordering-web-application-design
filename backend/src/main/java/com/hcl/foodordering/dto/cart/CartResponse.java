package com.hcl.foodordering.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long cartId,
        Long restaurantId,
        List<CartItemResponse> items,
        BigDecimal totalAmount
) {
}

