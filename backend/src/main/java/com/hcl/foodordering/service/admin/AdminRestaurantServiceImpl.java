package com.hcl.foodordering.service.admin;

import com.hcl.foodordering.dto.admin.CreateRestaurantRequest;
import com.hcl.foodordering.dto.admin.UpdateRestaurantRequest;
import com.hcl.foodordering.dto.restaurant.RestaurantResponse;
import com.hcl.foodordering.entity.Restaurant;
import com.hcl.foodordering.exception.ResourceNotFoundException;
import com.hcl.foodordering.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminRestaurantServiceImpl implements AdminRestaurantService {

    private static final Logger log = LoggerFactory.getLogger(AdminRestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;

    public AdminRestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<RestaurantResponse> listAll(String query) {
        List<Restaurant> restaurants = (query == null || query.isBlank())
                ? restaurantRepository.findAll()
                : restaurantRepository.findByNameContainingIgnoreCase(query.trim());

        return restaurants.stream().map(this::toResponse).toList();
    }

    @Override
    public RestaurantResponse create(CreateRestaurantRequest request) {
        Restaurant r = new Restaurant();
        r.setName(request.name().trim());
        r.setCuisine(request.cuisine());
        r.setAddress(request.address().trim());
        r.setRating(request.rating());
        r.setImageUrl(request.imageUrl());

        Restaurant saved = restaurantRepository.save(r);
        log.info("Admin created restaurant id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public RestaurantResponse update(Long restaurantId, UpdateRestaurantRequest request) {
        Restaurant r = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("RESTAURANT_NOT_FOUND", "Restaurant not found."));

        r.setName(request.name().trim());
        r.setCuisine(request.cuisine());
        r.setAddress(request.address().trim());
        r.setRating(request.rating());
        r.setImageUrl(request.imageUrl());

        Restaurant saved = restaurantRepository.save(r);
        log.info("Admin updated restaurant id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public void delete(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("RESTAURANT_NOT_FOUND", "Restaurant not found.");
        }
        restaurantRepository.deleteById(restaurantId);
        log.info("Admin deleted restaurant id={}", restaurantId);
    }

    private RestaurantResponse toResponse(Restaurant r) {
        return new RestaurantResponse(
                r.getId(),
                r.getName(),
                r.getCuisine(),
                r.getAddress(),
                r.getRating(),
                r.getImageUrl()
        );
    }
}

