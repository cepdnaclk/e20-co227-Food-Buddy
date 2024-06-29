package com.foodBudy_v2.demo.repository;

import com.foodBudy_v2.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
