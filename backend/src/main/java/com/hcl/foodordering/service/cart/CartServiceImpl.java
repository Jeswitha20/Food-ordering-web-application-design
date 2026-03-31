package com.hcl.foodordering.service.cart;

import com.hcl.foodordering.dto.cart.AddToCartRequest;
import com.hcl.foodordering.dto.cart.CartItemResponse;
import com.hcl.foodordering.dto.cart.CartResponse;
import com.hcl.foodordering.dto.cart.UpdateCartItemRequest;
import com.hcl.foodordering.entity.Cart;
import com.hcl.foodordering.entity.CartItem;
import com.hcl.foodordering.entity.MenuItem;
import com.hcl.foodordering.entity.User;
import com.hcl.foodordering.exception.BadRequestException;
import com.hcl.foodordering.exception.ResourceNotFoundException;
import com.hcl.foodordering.repository.CartItemRepository;
import com.hcl.foodordering.repository.CartRepository;
import com.hcl.foodordering.repository.MenuItemRepository;
import com.hcl.foodordering.repository.UserRepository;
import com.hcl.foodordering.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;

    public CartServiceImpl(
            UserRepository userRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            MenuItemRepository menuItemRepository
    ) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    @Transactional
    public CartResponse getCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseGet(() -> createEmptyCart(user));

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(AddToCartRequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseGet(() -> createEmptyCart(user));

        MenuItem menuItem = menuItemRepository.findByIdAndAvailableTrue(request.menuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MENU_ITEM_NOT_FOUND", "Menu item not found or unavailable."));

        enforceSingleRestaurant(cart, menuItem);

        var existing = cartItemRepository.findByCart_IdAndMenuItem_Id(cart.getId(), menuItem.getId());
        CartItem cartItem;
        if (existing.isPresent()) {
            cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(request.quantity());
            cart.getItems().add(cartItem);
        }

        Cart saved = cartRepository.save(cart);
        log.debug("Added to cart user={} menuItem={} qty={}", user.getEmail(), menuItem.getId(), request.quantity());
        return buildCartResponse(saved);
    }

    @Override
    @Transactional
    public CartResponse updateItem(Long menuItemId, UpdateCartItemRequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("CART_NOT_FOUND", "Cart is empty."));

        if (request.quantity() <= 0) {
            throw new BadRequestException("INVALID_QUANTITY", "Quantity must be >= 1.");
        }

        CartItem cartItem = cartItemRepository.findByCart_IdAndMenuItem_Id(cart.getId(), menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CART_ITEM_NOT_FOUND", "Cart item not found."));

        cartItem.setQuantity(request.quantity());
        cartRepository.save(cart);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeItem(Long menuItemId) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("CART_NOT_FOUND", "Cart is empty."));

        CartItem cartItem = cartItemRepository.findByCart_IdAndMenuItem_Id(cart.getId(), menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CART_ITEM_NOT_FOUND", "Cart item not found."));

        // Remove from the in-memory collection so that buildCartResponse
        // reflects the deletion immediately; orphanRemoval will handle DB delete.
        cart.getItems().remove(cartItem);
        Cart saved = cartRepository.save(cart);

        return buildCartResponse(saved);
    }

    private User getCurrentUser() {
        String email = SecurityUtils.getCurrentEmail();
        if (email == null) {
            throw new BadRequestException("UNAUTHENTICATED", "Authentication required.");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "Authenticated user not found."));
    }

    private Cart createEmptyCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    private void enforceSingleRestaurant(Cart cart, MenuItem menuItem) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return;
        }
        Long existingRestaurantId = cart.getItems().get(0).getMenuItem().getRestaurant().getId();
        Long incomingRestaurantId = menuItem.getRestaurant().getId();
        if (!existingRestaurantId.equals(incomingRestaurantId)) {
            throw new BadRequestException(
                    "CART_RESTAURANT_MISMATCH",
                    "Cart can contain items from only one restaurant."
            );
        }
    }

    private CartResponse buildCartResponse(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        var items = new java.util.ArrayList<CartItemResponse>();

        for (CartItem ci : cart.getItems()) {
            MenuItem mi = ci.getMenuItem();
            BigDecimal line = mi.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            total = total.add(line);
            items.add(new CartItemResponse(
                    ci.getId(),
                    mi.getId(),
                    mi.getName(),
                    mi.getDescription(),
                    mi.getPrice(),
                    ci.getQuantity(),
                    line
            ));
        }

        Long restaurantId = null;
        if (!items.isEmpty()) {
            restaurantId = cart.getItems().get(0).getMenuItem().getRestaurant().getId();
        }

        return new CartResponse(cart.getId(), restaurantId, items, total);
    }
}

