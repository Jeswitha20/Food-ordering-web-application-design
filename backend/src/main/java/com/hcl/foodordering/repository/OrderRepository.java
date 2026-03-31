package com.hcl.foodordering.repository;

import com.hcl.foodordering.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);

    Optional<Order> findByIdAndUser_Id(Long orderId, Long userId);
}

