package com.hcl.foodordering.service.user;

import com.hcl.foodordering.dto.user.UpdateUserRequest;
import com.hcl.foodordering.dto.user.UserMeResponse;
import com.hcl.foodordering.entity.User;
import com.hcl.foodordering.exception.BadRequestException;
import com.hcl.foodordering.exception.ResourceNotFoundException;
import com.hcl.foodordering.repository.UserRepository;
import com.hcl.foodordering.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserMeResponse getMe() {
        User user = getCurrentUser();
        return new UserMeResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    @Override
    @Transactional
    public UserMeResponse updateMe(UpdateUserRequest request) {
        if (request.password() == null || request.password().isBlank()) {
            throw new BadRequestException("INVALID_PASSWORD", "Password must not be blank.");
        }

        User user = getCurrentUser();
        user.setName(request.name().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        User saved = userRepository.save(user);
        log.info("Updated profile for {}", saved.getEmail());
        return new UserMeResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
    }

    @Override
    @Transactional
    public void deleteMe() {
        User user = getCurrentUser();
        userRepository.delete(user);
        log.info("Deleted user {}", user.getEmail());
    }

    private User getCurrentUser() {
        String email = SecurityUtils.getCurrentEmail();
        if (email == null) {
            throw new BadRequestException("UNAUTHENTICATED", "Authentication required.");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "Authenticated user not found."));
    }
}

