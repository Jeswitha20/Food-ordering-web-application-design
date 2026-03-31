package com.hcl.foodordering.service.admin;

import com.hcl.foodordering.dto.admin.CreateMenuItemRequest;
import com.hcl.foodordering.dto.admin.UpdateMenuItemRequest;
import com.hcl.foodordering.dto.restaurant.MenuItemResponse;
import com.hcl.foodordering.entity.MenuItem;
import com.hcl.foodordering.entity.Restaurant;
import com.hcl.foodordering.exception.ResourceNotFoundException;
import com.hcl.foodordering.repository.MenuItemRepository;
import com.hcl.foodordering.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminMenuItemServiceImpl implements AdminMenuItemService {

    private static final Logger log = LoggerFactory.getLogger(AdminMenuItemServiceImpl.class);

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public AdminMenuItemServiceImpl(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<MenuItemResponse> listByRestaurant(Long restaurantId) {
        List<MenuItem> items = menuItemRepository.findByRestaurant_Id(restaurantId);
        return items.stream().map(this::toResponse).toList();
    }

    @Override
    public MenuItemResponse create(Long restaurantId, CreateMenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("RESTAURANT_NOT_FOUND", "Restaurant not found."));

        MenuItem mi = new MenuItem();
        mi.setRestaurant(restaurant);
        mi.setName(request.name().trim());
        mi.setDescription(request.description());
        mi.setPrice(request.price());
        mi.setAvailable(request.available());

        MenuItem saved = menuItemRepository.save(mi);
        log.info("Admin created menu item id={} restaurantId={}", saved.getId(), restaurantId);
        return toResponse(saved);
    }

    @Override
    public MenuItemResponse update(Long menuItemId, UpdateMenuItemRequest request) {
        MenuItem mi = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("MENU_ITEM_NOT_FOUND", "Menu item not found."));

        mi.setName(request.name().trim());
        mi.setDescription(request.description());
        mi.setPrice(request.price());
        mi.setAvailable(request.available());

        MenuItem saved = menuItemRepository.save(mi);
        log.info("Admin updated menu item id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public void delete(Long menuItemId) {
        if (!menuItemRepository.existsById(menuItemId)) {
            throw new ResourceNotFoundException("MENU_ITEM_NOT_FOUND", "Menu item not found.");
        }
        menuItemRepository.deleteById(menuItemId);
        log.info("Admin deleted menu item id={}", menuItemId);
    }

    private MenuItemResponse toResponse(MenuItem mi) {
        return new MenuItemResponse(
                mi.getId(),
                mi.getName(),
                mi.getDescription(),
                mi.getPrice(),
                mi.isAvailable()
        );
    }
}

