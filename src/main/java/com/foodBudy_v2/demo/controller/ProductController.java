package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // create a product
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping ("/products/categories/{categoryId}")
    public ResponseEntity<ProductDTO> addProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long categoryId){

        ProductDTO createdProduct = productService.addProduct(productDTO, categoryId);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }


}
