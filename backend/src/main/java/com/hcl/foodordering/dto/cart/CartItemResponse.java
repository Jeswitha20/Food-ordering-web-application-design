package com.hcl.foodordering.dto.cart;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long menuItemId,
        String name,
        String description,
        BigDecimal price,
        int quantity,
        BigDecimal lineTotal
) {
}

