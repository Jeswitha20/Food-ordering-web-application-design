package com.hcl.foodordering.controller;

import com.hcl.foodordering.dto.restaurant.RestaurantDetailResponse;
import com.hcl.foodordering.dto.restaurant.RestaurantResponse;
import com.hcl.foodordering.service.restaurant.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> list(
            @RequestParam(name = "q", required = false) String query
    ) {
        log.debug("Listing restaurants with query={}", query);
        return ResponseEntity.ok(restaurantService.listRestaurants(query));
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDetailResponse> detail(
            @PathVariable Long restaurantId,
            @RequestParam(name = "menuQ", required = false) String menuQuery
    ) {
        log.debug("Restaurant detail restaurantId={}", restaurantId);
        return ResponseEntity.ok(restaurantService.getRestaurantDetail(restaurantId, menuQuery));
    }
}

