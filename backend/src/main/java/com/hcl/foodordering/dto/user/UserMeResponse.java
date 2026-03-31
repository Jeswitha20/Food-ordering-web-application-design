package com.hcl.foodordering.dto.user;

import com.hcl.foodordering.entity.enums.UserRole;

public record UserMeResponse(
        Long id,
        String name,
        String email,
        UserRole role
) {
}

