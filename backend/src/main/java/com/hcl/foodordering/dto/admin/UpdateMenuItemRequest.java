package com.hcl.foodordering.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateMenuItemRequest(
        @NotBlank String name,
        @Size(max = 1000) String description,
        @NotNull @Positive BigDecimal price,
        boolean available
) {
}

