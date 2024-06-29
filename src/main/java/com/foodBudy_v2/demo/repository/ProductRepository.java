package com.foodBudy_v2.demo.repository;

import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryOrderByDiscountedPriceAsc(Pageable pageDetails, Category category);

    Page<Product> findByProductNameLikeIgnoreCaseOrderByDiscountedPriceAsc(String s, Pageable pageDetails);
}
