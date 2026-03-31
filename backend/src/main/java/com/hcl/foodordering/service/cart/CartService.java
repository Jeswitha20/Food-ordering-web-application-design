package com.hcl.foodordering.service.cart;

import com.hcl.foodordering.dto.cart.AddToCartRequest;
import com.hcl.foodordering.dto.cart.CartResponse;
import com.hcl.foodordering.dto.cart.UpdateCartItemRequest;

public interface CartService {
    CartResponse getCart();

    CartResponse addItem(AddToCartRequest request);

    CartResponse updateItem(Long menuItemId, UpdateCartItemRequest request);

    CartResponse removeItem(Long menuItemId);
}

