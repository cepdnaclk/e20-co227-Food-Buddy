package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.payload.CategoryDTO;
import com.foodBudy_v2.demo.payload.CategoryResponse;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    /**
     * Get all categories
     * @return CategoryResponse
     */

    @GetMapping("/public/categories")
    @Operation(summary = "Get all categories" , description = "Retrieve details about all categories")
    public ResponseEntity<CategoryResponse> getAllCategories(){
        CategoryResponse categoryResponse = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    /**
     * Create a new category
     * @param categoryDTO
     * @return CategoryDTO
     */
    @PostMapping("/admin/categories")
    @Operation(summary = "Create a new category" , description = "Add a new category")
    @RequestBody(description = "Category Data transfer object that needs to be added",required = true)
    public ResponseEntity<CategoryDTO> CreateCategory(@Valid @RequestBody CategoryDTO categoryDTO){

        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);

        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    /**
     * Delete a category
     * @param categoryId
     * @return CategoryDTO
     */
    @DeleteMapping("/admin/categories/{categoryId}")
    @Operation(summary = "Delete a category" , description = "Remove a category from the category by its category ID")
    @Parameter(name = "categoryId" , description =  "Unique identifier of the category that needs to be deleted" , required = true)
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){

        CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);

        return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
    }

    /**
     * Update a category
     * @param categoryDTO
     * @param categoryId
     * @return CategoryDTO
     */
    @PutMapping("/admin/categories/{categoryId}")
    @Operation(summary = "Update a category" , description = "Update details about a category")
    @RequestBody(description = "Newly updated data transfer object of the category" , required = true)
    @Parameter(name = "categoryId" , description = "Unique identifier of the category" , required = true)
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                      @PathVariable Long categoryId){

        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);

        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);

    }

    // update the category image
    @PutMapping("/admin/categories/{categoryId}/image")
    @Operation(summary = "Update the category image", description = "Updates the image of a particular category by its ID.")
    @Parameter(name = "categoryId", description = "The unique identifier of the category to update the image.")
    @Parameter(name = "image", description = "The image file to upload", required = true)
    public ResponseEntity<CategoryDTO> updateCategoryImage(
            @PathVariable Long categoryId, @RequestParam("image") MultipartFile image)
            throws IOException {

        CategoryDTO updatedCategoryDTO =  categoryService.updateCategoryImage(categoryId, image);

        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }

    // download the category image
    @GetMapping("/public/categories/images/{fileName}")
    @Operation(summary = "Download the category image", description = "Downloads the image file of a category by its file name.")
    @Parameter(name = "fileName", description = "The file name of the category image to download.")
    public ResponseEntity<?> getCategoryImage(@PathVariable String fileName) throws IOException {

        byte [] photoData = categoryService.downloadProductImage(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(photoData);

    }

}
