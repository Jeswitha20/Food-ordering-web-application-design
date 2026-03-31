package com.hcl.foodordering.service.restaurant;

import com.hcl.foodordering.dto.restaurant.MenuItemResponse;
import com.hcl.foodordering.dto.restaurant.RestaurantDetailResponse;
import com.hcl.foodordering.dto.restaurant.RestaurantResponse;
import com.hcl.foodordering.entity.MenuItem;
import com.hcl.foodordering.entity.Restaurant;
import com.hcl.foodordering.exception.ResourceNotFoundException;
import com.hcl.foodordering.repository.MenuItemRepository;
import com.hcl.foodordering.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<RestaurantResponse> listRestaurants(String query) {
        List<Restaurant> restaurants;
        if (query == null || query.isBlank()) {
            restaurants = restaurantRepository.findAll();
        } else {
            restaurants = restaurantRepository.findByNameContainingIgnoreCase(query.trim());
        }

        return restaurants.stream()
                .map(this::toRestaurantResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantDetailResponse getRestaurantDetail(Long restaurantId, String menuQuery) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("RESTAURANT_NOT_FOUND", "Restaurant not found."));

        List<MenuItem> menuItems;
        if (menuQuery == null || menuQuery.isBlank()) {
            menuItems = menuItemRepository.findByRestaurant_IdAndAvailableTrue(restaurantId);
        } else {
            menuItems = menuItemRepository.searchAvailableInRestaurant(restaurantId, menuQuery.trim());
        }

        List<MenuItemResponse> menu = menuItems.stream()
                .map(this::toMenuItemResponse)
                .collect(Collectors.toList());

        return new RestaurantDetailResponse(toRestaurantResponse(restaurant), menu);
    }

    private RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCuisine(),
                restaurant.getAddress(),
                restaurant.getRating(),
                restaurant.getImageUrl()
        );
    }

    private MenuItemResponse toMenuItemResponse(MenuItem m) {
        return new MenuItemResponse(
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.getPrice(),
                m.isAvailable()
        );
    }
}

