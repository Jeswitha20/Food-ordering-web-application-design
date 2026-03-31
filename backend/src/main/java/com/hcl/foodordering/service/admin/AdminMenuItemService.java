package com.hcl.foodordering.service.admin;

import com.hcl.foodordering.dto.admin.CreateMenuItemRequest;
import com.hcl.foodordering.dto.admin.UpdateMenuItemRequest;
import com.hcl.foodordering.dto.restaurant.MenuItemResponse;

import java.util.List;

public interface AdminMenuItemService {
    List<MenuItemResponse> listByRestaurant(Long restaurantId);

    MenuItemResponse create(Long restaurantId, CreateMenuItemRequest request);

    MenuItemResponse update(Long menuItemId, UpdateMenuItemRequest request);

    void delete(Long menuItemId);
}

