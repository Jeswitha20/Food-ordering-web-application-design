package com.hcl.foodordering.dto.restaurant;

import java.util.List;

public record RestaurantDetailResponse(
        RestaurantResponse restaurant,
        List<MenuItemResponse> menu
) {
}

