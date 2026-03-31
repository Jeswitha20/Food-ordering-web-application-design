package com.hcl.foodordering.service.restaurant;

import com.hcl.foodordering.dto.restaurant.RestaurantDetailResponse;
import com.hcl.foodordering.dto.restaurant.RestaurantResponse;

import java.util.List;

public interface RestaurantService {
    List<RestaurantResponse> listRestaurants(String query);

    RestaurantDetailResponse getRestaurantDetail(Long restaurantId, String menuQuery);
}

