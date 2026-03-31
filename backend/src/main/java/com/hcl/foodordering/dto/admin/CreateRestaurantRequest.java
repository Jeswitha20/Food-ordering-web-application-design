package com.hcl.foodordering.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRestaurantRequest(
        @NotBlank String name,
        String cuisine,
        @NotBlank @Size(max = 255) String address,
        Double rating,
        String imageUrl
) {
}

