package com.hcl.foodordering.dto.order;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderRequest(
        @NotBlank String deliveryAddress,
        String instructions
) {
}

