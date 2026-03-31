package com.hcl.foodordering.service.auth;

import com.hcl.foodordering.dto.auth.AuthResponse;
import com.hcl.foodordering.dto.auth.LoginRequest;
import com.hcl.foodordering.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void signOut();
}

