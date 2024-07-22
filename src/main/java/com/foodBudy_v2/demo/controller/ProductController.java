package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.config.AppConstants;
import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.payload.APIResponse;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.payload.ProductResponse;
import com.foodBudy_v2.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // create a product
    @PostMapping ("/products/categories/{categoryId}")
    public ResponseEntity<ProductDTO> addProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long categoryId){

        ProductDTO createdProduct = productService.addProduct(productDTO, categoryId);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // get all products
    @GetMapping ("public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER)
            Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)
            Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY)
            String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION)
            String sortOrder
    ){

        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        // no products
        if (productResponse.getContent().isEmpty()){
            throw new APIException("no products!");
        }

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // get products by category
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER)
            Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)
            Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY)
            String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION)
            String sortOrder
    ){

        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // get products by keyword
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER)
            Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)
            Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY)
            String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION)
            String sortOrder
    ){

        ProductResponse productResponse = productService.searchByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // get nearby products
    @GetMapping("/public/products/nearby")
    public ResponseEntity<ProductResponse> getNearbyProducts(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius
    ){
        ProductResponse productResponse = productService.getNearbyProducts(latitude, longitude, radius);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // get products by shopId
    @GetMapping("/public/products/shops/{shopId}")
    public ResponseEntity<ProductResponse> getProductsByShop(
            @PathVariable Long shopId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER)
            Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)
            Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY)
            String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION)
            String sortOrder

    ){
        ProductResponse productResponse = productService.getProductsByShop(shopId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    // update the product image
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId, @RequestParam("image") MultipartFile image)
            throws IOException {

        ProductDTO updatedProductDTO =  productService.updateProductImage(productId, image);

        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }


    // download the product image
    @GetMapping("/public/products/images/{fileName}")
    public ResponseEntity<?> getProductImage(@PathVariable String fileName) throws IOException {

        byte [] photoData = productService.downloadProductImage(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(photoData);

    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<APIResponse> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>(new APIResponse("Deleted successfully", true), HttpStatus.OK);

    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long productId){

        ProductDTO updatedProduct = productService.updateProduct(productDTO, productId);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }



}
