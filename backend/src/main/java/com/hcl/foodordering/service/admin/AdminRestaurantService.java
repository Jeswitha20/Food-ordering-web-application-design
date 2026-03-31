package com.hcl.foodordering.service.admin;

import com.hcl.foodordering.dto.admin.CreateRestaurantRequest;
import com.hcl.foodordering.dto.admin.UpdateRestaurantRequest;
import com.hcl.foodordering.dto.restaurant.RestaurantResponse;

import java.util.List;

public interface AdminRestaurantService {
    List<RestaurantResponse> listAll(String query);

    RestaurantResponse create(CreateRestaurantRequest request);

    RestaurantResponse update(Long restaurantId, UpdateRestaurantRequest request);

    void delete(Long restaurantId);
}

