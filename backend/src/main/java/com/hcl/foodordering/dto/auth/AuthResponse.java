package com.hcl.foodordering.dto.auth;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Long userId,
        String name,
        String email,
        String role
) {
    public static AuthResponse of(String accessToken, Long userId, String name, String email, String role) {
        return new AuthResponse(accessToken, "Bearer", userId, name, email, role);
    }
}

