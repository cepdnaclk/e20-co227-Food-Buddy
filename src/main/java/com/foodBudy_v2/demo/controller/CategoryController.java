package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.payload.CategoryDTO;
import com.foodBudy_v2.demo.payload.CategoryResponse;
import com.foodBudy_v2.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                      @PathVariable Long categoryId){

        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);

        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);

    }

}
