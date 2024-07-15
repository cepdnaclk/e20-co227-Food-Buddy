package com.foodBudy_v2.demo.repository;

import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryOrderByDiscountedPriceAsc(Pageable pageDetails, Category category);

    Page<Product> findByProductNameLikeIgnoreCaseOrderByDiscountedPriceAsc(String s, Pageable pageDetails);

    @Query("SELECT p FROM Product p WHERE p.shop.shopId = ?1")
    Page<Product> findAllByShopId(Long shopId, Pageable pageDetails);

}
