package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    byte[] downloadProductImage(String fileName) throws IOException;

    ProductResponse getNearbyProducts(double latitude, double longitude, double radius);

    ProductResponse getProductsByShop(Long shopId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    void deleteProduct(Long productId);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO getProductById(Long productId);
}
