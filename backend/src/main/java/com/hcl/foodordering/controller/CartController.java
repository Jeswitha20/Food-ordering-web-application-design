package com.hcl.foodordering.controller;

import com.hcl.foodordering.dto.cart.AddToCartRequest;
import com.hcl.foodordering.dto.cart.CartResponse;
import com.hcl.foodordering.dto.cart.UpdateCartItemRequest;
import com.hcl.foodordering.service.cart.CartService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@Valid @RequestBody AddToCartRequest request) {
        log.debug("Add to cart: menuItemId={} qty={}", request.menuItemId(), request.quantity());
        return ResponseEntity.ok(cartService.addItem(request));
    }

    @PatchMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponse> updateItem(
            @PathVariable Long menuItemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return ResponseEntity.ok(cartService.updateItem(menuItemId, request));
    }

    @DeleteMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long menuItemId) {
        return ResponseEntity.ok(cartService.removeItem(menuItemId));
    }
}

