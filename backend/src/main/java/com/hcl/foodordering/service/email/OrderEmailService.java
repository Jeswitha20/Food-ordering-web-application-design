package com.hcl.foodordering.service.email;

import com.hcl.foodordering.entity.Order;
import com.hcl.foodordering.entity.User;

public interface OrderEmailService {
    boolean sendOrderConfirmation(User user, Order order);

    boolean sendOrderCancellation(User user, Order order);
}

