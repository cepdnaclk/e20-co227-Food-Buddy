package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.config.AppConstants;
import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.exception.ResourceNotFoundException;
import com.foodBudy_v2.demo.payload.APIResponse;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.payload.ProductResponse;
import com.foodBudy_v2.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    @Operation(summary = "Create a product" , description = "Add a new product to the user's shop")
    @RequestBody(description = "Product Data transfer Object that needs to be created" , required = true)
    @Parameter(name = "categoryID" , description = "unique identifier of the category of the product", required = true)
    public ResponseEntity<ProductDTO> addProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long categoryId){

        ProductDTO createdProduct = productService.addProduct(productDTO, categoryId);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // get all products
    @GetMapping ("public/products")
    @Operation(summary = "Get all product" , description = "Retrive details of all products")
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

    // get product by productId
    @GetMapping ("public/products/{productId}")
    @Operation(summary = "Get product by productId" , description = "Retrive product details by its unique identifier")
    @Parameter(name = "productId" , description = "Unique identifier of the product" , required = true)
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId){
        ProductDTO productDTO = productService.getProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }


    // get products by category
    @GetMapping("/public/categories/{categoryId}/products")
    @Operation(summary = "Get product by category" , description = "Retrive details about products that belongs to same category")
    @Parameter(name = "categoryId" , description = "Unique identifier of the category " , required = true)
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
    @Operation(summary = "Get products by keyword" , description = "Retrive details about products by its name")
    @Parameter(name = "keyword" , description = "The keyword used to search for products by name")
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
    @Operation(summary = "Get nearby products" , description = "Retrieve details about products that situated within a certain range of the user's location")
    @Parameter(name = "latitude", description = "The user's latitude for finding nearby products")
    @Parameter(name = "longitude", description = "The user's longitude for finding nearby products")
    @Parameter(name = "radius", description = "The radius within which to search for products")
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
    @Operation(summary = "Get products by shopID" , description = "Retrieve details about products which are available in a certain shop")
    @Parameter(name = "shopId" , description = "Unique identifier of the shop" , example = "2")
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
    @Operation(summary = "Update the product image", description = "Update the image of a product by providing a new image file.")
    @Parameter(name = "productId", description = "Unique identifier of the product")
    @Parameter(name = "image", description = "The new product image file")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId, @RequestParam("image") MultipartFile image)
            throws IOException {

        ProductDTO updatedProductDTO =  productService.updateProductImage(productId, image);

        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }


    // download the product image
    @GetMapping("/public/products/images/{fileName}")
    @Operation(summary = "Download the product image", description = "Downloads the image file for a given product by the file name.")
    @Parameter(name = "fileName", description = "The file name of the product image to be downloaded.")
    public ResponseEntity<?> getProductImage(@PathVariable String fileName) throws IOException {

        byte [] photoData = productService.downloadProductImage(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(photoData);

    }

    @DeleteMapping("/products/{productId}")
    @Operation(summary = "Delete a product", description = "Deletes a product by its unique identifier (productId).")
    @Parameter(name = "productId", description = "Unique identifier of the product to be deleted.")
    public ResponseEntity<APIResponse> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(new APIResponse("Deleted successfully", true), HttpStatus.OK);

    }

    @PutMapping("/products/{productId}")
    @Operation(summary = "Update a product", description = "Updates the details of a product by its ID.")
    @Parameter(name = "productId", description = "Unique identifier of the product to be updated.")
    public ResponseEntity<ProductDTO> updateProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long productId){

        ProductDTO updatedProduct = productService.updateProduct(productDTO, productId);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }



}
