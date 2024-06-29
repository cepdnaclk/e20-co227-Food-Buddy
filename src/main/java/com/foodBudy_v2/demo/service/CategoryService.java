package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.payload.CategoryDTO;
import com.foodBudy_v2.demo.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories();

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
