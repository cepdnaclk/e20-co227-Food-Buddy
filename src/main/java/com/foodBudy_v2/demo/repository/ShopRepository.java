package com.foodBudy_v2.demo.repository;

import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Boolean existsByOwner(AppUser user);

    Optional<Shop> findByOwner(AppUser user);
}
