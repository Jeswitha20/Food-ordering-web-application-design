package com.hcl.foodordering.service.auth;

import com.hcl.foodordering.dto.auth.AuthResponse;
import com.hcl.foodordering.dto.auth.LoginRequest;
import com.hcl.foodordering.dto.auth.RegisterRequest;
import com.hcl.foodordering.entity.User;
import com.hcl.foodordering.entity.enums.UserRole;
import com.hcl.foodordering.exception.ConflictException;
import com.hcl.foodordering.exception.UnauthorizedException;
import com.hcl.foodordering.repository.UserRepository;
import com.hcl.foodordering.service.email.RegistrationEmailService;
import com.hcl.foodordering.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RegistrationEmailService registrationEmailService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RegistrationEmailService registrationEmailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.registrationEmailService = registrationEmailService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("EMAIL_TAKEN", "Email is already registered.");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.CUSTOMER); // placeholder for now

        User saved = userRepository.save(user);
        log.info("Registered new user: {}", saved.getEmail());

        boolean emailSent = registrationEmailService.sendRegistrationSuccess(saved);
        if (emailSent) {
            log.info("Registration email sent to {}", saved.getEmail());
        } else {
            log.warn("Registration email NOT sent to {} (check mail config)", saved.getEmail());
        }

        String token = jwtService.generateAccessToken(saved.getEmail(), saved.getRole().name());
        return AuthResponse.of(token, saved.getId(), saved.getName(), saved.getEmail(), saved.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("INVALID_CREDENTIALS", "Invalid email or password."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("INVALID_CREDENTIALS", "Invalid email or password.");
        }

        String token = jwtService.generateAccessToken(user.getEmail(), user.getRole().name());
        log.info("User logged in: {}", email);
        return AuthResponse.of(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    @Override
    public void signOut() {
        // Stateless JWT: client discards token.
        log.debug("Sign out requested (stateless JWT).");
    }
}

