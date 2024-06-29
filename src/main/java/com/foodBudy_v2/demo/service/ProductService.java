package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.payload.ProductDTO;
import org.springframework.stereotype.Service;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
}
