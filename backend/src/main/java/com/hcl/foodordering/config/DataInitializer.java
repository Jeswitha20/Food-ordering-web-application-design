package com.hcl.foodordering.config;

import com.hcl.foodordering.entity.MenuItem;
import com.hcl.foodordering.entity.Restaurant;
import com.hcl.foodordering.entity.User;
import com.hcl.foodordering.entity.enums.UserRole;
import com.hcl.foodordering.repository.MenuItemRepository;
import com.hcl.foodordering.repository.RestaurantRepository;
import com.hcl.foodordering.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private record MenuSeed(String name, String description, BigDecimal price, boolean available) {
    }

    private record RestaurantSeed(String name, String cuisine, String address, Double rating, String imageUrl, List<MenuSeed> menu) {
    }

    @Bean
    public CommandLineRunner seedData(
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@hcl.local");
                admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
                admin.setRole(UserRole.ADMIN);
                userRepository.save(admin);
                log.info("Seeded ADMIN user: {}", admin.getEmail());
            }

            // Add the initial restaurants (if missing) plus ~10 more for a better demo.
            List<RestaurantSeed> seeds = List.of(
                    new RestaurantSeed(
                            "Spice & Slice",
                            "Italian",
                            "123 Main Street",
                            4.6,
                            "https://images.pexels.com/photos/825661/pexels-photo-825661.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Margherita Pizza", "Classic cheese and tomato pizza.", new BigDecimal("399.00"), true),
                                    new MenuSeed("Pasta Alfredo", "Creamy Alfredo sauce with herbs.", new BigDecimal("449.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Biryani Blaze",
                            "Indian",
                            "221 Market Road",
                            4.4,
                            "https://images.pexels.com/photos/1624487/pexels-photo-1624487.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Chicken Biryani", "Aromatic basmati rice with tender chicken.", new BigDecimal("349.00"), true),
                                    new MenuSeed("Veg Korma", "Vegetables in a rich cashew gravy.", new BigDecimal("299.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Urban Tandoor",
                            "Indian",
                            "77 Spice Avenue",
                            4.5,
                            "https://images.pexels.com/photos/7245466/pexels-photo-7245466.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Tandoori Chicken", "Smoky grilled chicken with tandoor spices.", new BigDecimal("379.00"), true),
                                    new MenuSeed("Garlic Naan", "Buttery garlic naan baked to perfection.", new BigDecimal("99.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Green Bowl",
                            "Healthy",
                            "15 Wellness Street",
                            4.3,
                            "https://images.pexels.com/photos/1640774/pexels-photo-1640774.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Quinoa Salad", "Fresh veggies with lemon-herb quinoa.", new BigDecimal("259.00"), true),
                                    new MenuSeed("Avocado Toast", "Creamy avocado on toasted sourdough.", new BigDecimal("199.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Burger Junction",
                            "American",
                            "9 Grill Road",
                            4.2,
                            "https://images.pexels.com/photos/1639562/pexels-photo-1639562.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Smash Burger", "Crispy smash patty with house sauce.", new BigDecimal("329.00"), true),
                                    new MenuSeed("Loaded Fries", "Fries topped with cheese and seasoning.", new BigDecimal("199.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Sushi Station",
                            "Japanese",
                            "42 Ocean Blvd",
                            4.6,
                            "https://images.pexels.com/photos/3298180/pexels-photo-3298180.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Salmon Roll", "Fresh salmon roll with special mayo.", new BigDecimal("499.00"), true),
                                    new MenuSeed("Veg Maki", "Crisp veggies with sesame soy glaze.", new BigDecimal("279.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Cream & Co.",
                            "Desserts",
                            "88 Sweet Lane",
                            4.4,
                            "https://images.pexels.com/photos/4109990/pexels-photo-4109990.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Chocolate Sundae", "Rich chocolate with vanilla ice cream.", new BigDecimal("219.00"), true),
                                    new MenuSeed("Mango Cheesecake", "Creamy cheesecake with mango swirl.", new BigDecimal("289.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Coastal Curries",
                            "Seafood",
                            "6 Harbor Street",
                            4.5,
                            "https://images.pexels.com/photos/6027508/pexels-photo-6027508.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Garlic Prawn Curry", "Prawns cooked in garlic-tomato gravy.", new BigDecimal("559.00"), true),
                                    new MenuSeed("Fish Fingers", "Crispy fish fingers with tangy dip.", new BigDecimal("299.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Mediterranean Harbor",
                            "Mediterranean",
                            "31 Olive Way",
                            4.1,
                            "https://images.pexels.com/photos/1435904/pexels-photo-1435904.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Falafel Wrap", "Crispy falafel with fresh veggies.", new BigDecimal("319.00"), true),
                                    new MenuSeed("Hummus & Pita", "Creamy hummus with warm pita bread.", new BigDecimal("179.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Thai Time",
                            "Thai",
                            "20 Lime Avenue",
                            4.3,
                            "https://images.pexels.com/photos/958545/pexels-photo-958545.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Pad Thai", "Stir-fried noodles with peanut sauce.", new BigDecimal("399.00"), true),
                                    new MenuSeed("Green Curry", "Fragrant green curry with veggies.", new BigDecimal("369.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "South Spice",
                            "South Indian",
                            "5 Dosa Corner",
                            4.4,
                            "https://images.pexels.com/photos/9609854/pexels-photo-9609854.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Masala Dosa", "Crispy dosa with spiced potato filling.", new BigDecimal("189.00"), true),
                                    new MenuSeed("Idli Sambar", "Soft idlis with lentil sambar.", new BigDecimal("149.00"), true)
                            )
                    ),
                    new RestaurantSeed(
                            "Italian Alley",
                            "Italian",
                            "60 Roma Street",
                            4.2,
                            "https://images.pexels.com/photos/1279330/pexels-photo-1279330.jpeg?auto=compress&cs=tinysrgb&w=800",
                            List.of(
                                    new MenuSeed("Garlic Bread", "Toasty garlic bread with herbs.", new BigDecimal("129.00"), true),
                                    new MenuSeed("Penne Arrabbiata", "Spicy tomato arrabbiata sauce penne.", new BigDecimal("349.00"), true)
                            )
                    )
            );

            List<Restaurant> existingRestaurants = restaurantRepository.findAll();
            Map<String, Restaurant> existingByName = new HashMap<>();
            for (Restaurant r : existingRestaurants) {
                if (r.getName() != null) {
                    existingByName.put(r.getName().toLowerCase(Locale.ROOT), r);
                }
            }

            for (RestaurantSeed seed : seeds) {
                String key = seed.name().toLowerCase(Locale.ROOT);
                Restaurant restaurant = existingByName.get(key);
                if (restaurant == null) {
                    restaurant = new Restaurant();
                    restaurant.setName(seed.name());
                    restaurant.setCuisine(seed.cuisine());
                    restaurant.setAddress(seed.address());
                    restaurant.setRating(seed.rating());
                    restaurant.setImageUrl(seed.imageUrl());
                    restaurant.setCreatedAt(Instant.now());
                } else {
                    // Backfill image/cuisine/address/rating if they were previously blank.
                    if (restaurant.getImageUrl() == null || restaurant.getImageUrl().isBlank()) {
                        restaurant.setImageUrl(seed.imageUrl());
                    }
                    if (restaurant.getCuisine() == null) {
                        restaurant.setCuisine(seed.cuisine());
                    }
                    if (restaurant.getAddress() == null) {
                        restaurant.setAddress(seed.address());
                    }
                    if (restaurant.getRating() == null) {
                        restaurant.setRating(seed.rating());
                    }
                }

                restaurant = restaurantRepository.save(restaurant);
                existingByName.put(key, restaurant);
                log.info("Seed sync for restaurant: {}", restaurant.getName());

                List<MenuItem> existingItems = menuItemRepository.findByRestaurant_Id(restaurant.getId());
                Set<String> existingItemNames = existingItems.stream()
                        .map(MenuItem::getName)
                        .filter(n -> n != null && !n.isBlank())
                        .map(n -> n.toLowerCase(Locale.ROOT))
                        .collect(Collectors.toSet());

                List<MenuItem> missing = new java.util.ArrayList<>();
                for (MenuSeed ms : seed.menu()) {
                    String itemKey = ms.name().toLowerCase(Locale.ROOT);
                    if (existingItemNames.contains(itemKey)) {
                        continue;
                    }
                    MenuItem mi = new MenuItem();
                    mi.setRestaurant(restaurant);
                    mi.setName(ms.name());
                    mi.setDescription(ms.description());
                    mi.setPrice(ms.price());
                    mi.setAvailable(ms.available());
                    missing.add(mi);
                }

                if (!missing.isEmpty()) {
                    menuItemRepository.saveAll(missing);
                    log.info("Seeded {} menu items for restaurant={}", missing.size(), restaurant.getName());
                }
            }

            log.info("Seed complete. Total restaurants now: {}", restaurantRepository.count());
        };
    }
}

