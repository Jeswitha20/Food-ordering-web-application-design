package com.hcl.foodordering.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank String name,
        @Size(min = 6, max = 100) String password
) {
}

