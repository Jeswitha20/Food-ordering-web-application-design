package com.hcl.foodordering.controller;

import com.hcl.foodordering.dto.user.UpdateUserRequest;
import com.hcl.foodordering.dto.user.UserMeResponse;
import com.hcl.foodordering.service.user.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> me() {
        return ResponseEntity.ok(userService.getMe());
    }

    @PutMapping("/me")
    public ResponseEntity<UserMeResponse> updateMe(@Valid @RequestBody UpdateUserRequest request) {
        log.debug("Updating profile for authenticated user");
        return ResponseEntity.ok(userService.updateMe(request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe() {
        userService.deleteMe();
        return ResponseEntity.ok().build();
    }
}

