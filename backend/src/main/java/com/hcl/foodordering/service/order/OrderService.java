package com.hcl.foodordering.service.order;

import com.hcl.foodordering.dto.order.OrderResponse;
import com.hcl.foodordering.dto.order.CancelOrderRequest;
import com.hcl.foodordering.dto.order.PlaceOrderRequest;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(PlaceOrderRequest request);

    List<OrderResponse> listMyOrders();

    OrderResponse cancelOrder(Long orderId, CancelOrderRequest request);
}

