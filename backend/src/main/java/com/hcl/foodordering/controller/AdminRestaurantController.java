package com.hcl.foodordering.controller;

import com.hcl.foodordering.dto.admin.CreateRestaurantRequest;
import com.hcl.foodordering.dto.admin.UpdateRestaurantRequest;
import com.hcl.foodordering.dto.restaurant.RestaurantResponse;
import com.hcl.foodordering.service.admin.AdminRestaurantService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/restaurants")
public class AdminRestaurantController {

    private static final Logger log = LoggerFactory.getLogger(AdminRestaurantController.class);

    private final AdminRestaurantService adminRestaurantService;

    public AdminRestaurantController(AdminRestaurantService adminRestaurantService) {
        this.adminRestaurantService = adminRestaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> list(
            @RequestParam(name = "q", required = false) String query
    ) {
        log.debug("Admin list restaurants q={}", query);
        return ResponseEntity.ok(adminRestaurantService.listAll(query));
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(
            @Valid @RequestBody CreateRestaurantRequest request
    ) {
        log.debug("Admin create restaurant {}", request.name());
        return ResponseEntity.ok(adminRestaurantService.create(request));
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable Long restaurantId,
            @Valid @RequestBody UpdateRestaurantRequest request
    ) {
        log.debug("Admin update restaurant id={}", restaurantId);
        return ResponseEntity.ok(adminRestaurantService.update(restaurantId, request));
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<?> delete(@PathVariable Long restaurantId) {
        log.debug("Admin delete restaurant id={}", restaurantId);
        adminRestaurantService.delete(restaurantId);
        return ResponseEntity.ok().build();
    }
}

