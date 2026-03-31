package com.hcl.foodordering.dto.restaurant;

import java.math.BigDecimal;

public record RestaurantResponse(
        Long id,
        String name,
        String cuisine,
        String address,
        Double rating,
        String imageUrl
) {
}

