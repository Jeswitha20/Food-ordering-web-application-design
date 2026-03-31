package com.hcl.foodordering.controller;

import com.hcl.foodordering.dto.admin.CreateMenuItemRequest;
import com.hcl.foodordering.dto.admin.UpdateMenuItemRequest;
import com.hcl.foodordering.dto.restaurant.MenuItemResponse;
import com.hcl.foodordering.service.admin.AdminMenuItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminMenuItemController {

    private static final Logger log = LoggerFactory.getLogger(AdminMenuItemController.class);

    private final AdminMenuItemService adminMenuItemService;

    public AdminMenuItemController(AdminMenuItemService adminMenuItemService) {
        this.adminMenuItemService = adminMenuItemService;
    }

    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItemResponse>> listByRestaurant(@PathVariable Long restaurantId) {
        log.debug("Admin list menu items for restaurantId={}", restaurantId);
        return ResponseEntity.ok(adminMenuItemService.listByRestaurant(restaurantId));
    }

    @PostMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<MenuItemResponse> create(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateMenuItemRequest request
    ) {
        log.debug("Admin create menu item restaurantId={}", restaurantId);
        return ResponseEntity.ok(adminMenuItemService.create(restaurantId, request));
    }

    @PutMapping("/menu-items/{menuItemId}")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable Long menuItemId,
            @Valid @RequestBody UpdateMenuItemRequest request
    ) {
        log.debug("Admin update menu item id={}", menuItemId);
        return ResponseEntity.ok(adminMenuItemService.update(menuItemId, request));
    }

    @DeleteMapping("/menu-items/{menuItemId}")
    public ResponseEntity<?> delete(@PathVariable Long menuItemId) {
        log.debug("Admin delete menu item id={}", menuItemId);
        adminMenuItemService.delete(menuItemId);
        return ResponseEntity.ok().build();
    }
}

