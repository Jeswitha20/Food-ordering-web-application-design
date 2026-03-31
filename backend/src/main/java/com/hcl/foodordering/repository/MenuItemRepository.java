package com.hcl.foodordering.repository;

import com.hcl.foodordering.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurant_IdAndAvailableTrue(Long restaurantId);

    Optional<MenuItem> findByIdAndAvailableTrue(Long id);

    List<MenuItem> findByRestaurant_Id(Long restaurantId);

    @Query("""
        select m
        from MenuItem m
        where m.available = true
          and m.restaurant.id = :restaurantId
          and (lower(m.name) like lower(concat('%', :query, '%'))
               or lower(m.description) like lower(concat('%', :query, '%')))
        """)
    List<MenuItem> searchAvailableInRestaurant(
            @Param("restaurantId") Long restaurantId,
            @Param("query") String query
    );
}

