package com.hcl.foodordering.service.order;

import com.hcl.foodordering.dto.order.OrderItemResponse;
import com.hcl.foodordering.dto.order.OrderResponse;
import com.hcl.foodordering.dto.order.CancelOrderRequest;
import com.hcl.foodordering.dto.order.PlaceOrderRequest;
import com.hcl.foodordering.entity.*;
import com.hcl.foodordering.entity.enums.OrderStatus;
import com.hcl.foodordering.exception.BadRequestException;
import com.hcl.foodordering.exception.ResourceNotFoundException;
import com.hcl.foodordering.repository.CartRepository;
import com.hcl.foodordering.repository.OrderRepository;
import com.hcl.foodordering.repository.UserRepository;
import com.hcl.foodordering.security.SecurityUtils;
import com.hcl.foodordering.service.email.OrderEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderEmailService orderEmailService;

    public OrderServiceImpl(
            UserRepository userRepository,
            CartRepository cartRepository,
            OrderRepository orderRepository,
            OrderEmailService orderEmailService
    ) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.orderEmailService = orderEmailService;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("CART_NOT_FOUND", "Cart is empty."));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("CART_EMPTY", "Add items to cart before placing an order.");
        }

        // Enforce single-restaurant cart rule (cart service also does this, but we re-check here).
        Restaurant restaurant = cart.getItems().get(0).getMenuItem().getRestaurant();
        for (CartItem cartItem : cart.getItems()) {
            if (!cartItem.getMenuItem().getRestaurant().getId().equals(restaurant.getId())) {
                throw new BadRequestException("CART_RESTAURANT_MISMATCH", "Cart items must belong to one restaurant.");
            }
        }

        BigDecimal total = cart.getItems().stream()
                .map(ci -> ci.getMenuItem().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(request.deliveryAddress().trim());
        order.setInstructions(request.instructions() == null ? null : request.instructions().trim());
        order.setTotalAmount(total);
        // Mocked checkout: treat successful placement as CONFIRMED immediately.
        order.setStatus(OrderStatus.CONFIRMED);

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(cartItem.getMenuItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getMenuItem().getPrice());
            order.getItems().add(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        // Mocked checkout: placing the order only (no payment integration).
        // Clear cart after order placement.
        cart.getItems().clear();
        cartRepository.save(cart);

        boolean emailSent = orderEmailService.sendOrderConfirmation(user, savedOrder);
        savedOrder.setEmailSent(emailSent);
        orderRepository.save(savedOrder);

        log.info("Order placed. orderId={} user={}", savedOrder.getId(), user.getEmail());
        return toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> listMyOrders() {
        User user = getCurrentUser();
        return orderRepository.findByUser_IdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, CancelOrderRequest request) {
        User user = getCurrentUser();

        Order order = orderRepository.findByIdAndUser_Id(orderId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", "Order not found."));

        if (order.getStatus() != OrderStatus.PLACED && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BadRequestException("ORDER_NOT_ACTIVE", "Only active orders can be cancelled.");
        }

        order.setStatus(OrderStatus.CANCELLED);

        // For mocked checkout there is no payment rollback; we only update status + send email.
        boolean emailSent = orderEmailService.sendOrderCancellation(user, order);
        order.setEmailSent(emailSent);

        // Clear any future status changes (placeholder)
        Order saved = orderRepository.save(order);
        log.info("Order cancelled. orderId={} user={}", saved.getId(), user.getEmail());
        return toOrderResponse(saved);
    }

    private User getCurrentUser() {
        String email = SecurityUtils.getCurrentEmail();
        if (email == null) {
            throw new BadRequestException("UNAUTHENTICATED", "Authentication required.");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "Authenticated user not found."));
    }

    private OrderResponse toOrderResponse(Order order) {
        Long restaurantId = order.getRestaurant().getId();
        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getMenuItem().getId(),
                        i.getMenuItem().getName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                restaurantId,
                order.getDeliveryAddress(),
                order.getInstructions(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                order.isEmailSent(),
                items
        );
    }
}

