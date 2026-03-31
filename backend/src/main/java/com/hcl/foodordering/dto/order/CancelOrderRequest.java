package com.hcl.foodordering.dto.order;

import jakarta.validation.constraints.Size;

public record CancelOrderRequest(
        @Size(max = 500) String reason
) {
}

