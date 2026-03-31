package com.hcl.foodordering.repository;

import com.hcl.foodordering.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart_Id(Long cartId);

    Optional<CartItem> findByCart_IdAndMenuItem_Id(Long cartId, Long menuItemId);

    Optional<CartItem> findByIdAndCart_Id(Long cartItemId, Long cartId);
}

