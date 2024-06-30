package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.exception.ResourceNotFoundException;
import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.payload.CategoryDTO;
import com.foodBudy_v2.demo.payload.CategoryResponse;
import com.foodBudy_v2.demo.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public CategoryResponse getAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        // convert the list of Category objects into a list of CategoryDTO objects
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        // create the response
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Optional<Category> categoryWithSameName = categoryRepository.findByCategoryNameIgnoreCase(categoryDTO.getCategoryName());

        if (categoryWithSameName.isPresent()){
            throw new APIException("Category with the name " + categoryDTO.getCategoryName() + " already exists!");
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        //category.setCategoryId(null);

        Category savedCategory =  categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        // find the category object with the given id
        Optional<Category> opCategory = categoryRepository.findById(categoryId);

        if (opCategory.isPresent()){
            Category category = opCategory.get();
            categoryRepository.delete(category);

            return modelMapper.map(category, CategoryDTO.class);
        }
        throw new ResourceNotFoundException("Category", "categoryId", categoryId);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO changedCategoryDTO, Long categoryId) {
        Optional<Category> opCategory = categoryRepository.findById(categoryId);

        if (opCategory.isPresent()){
            changedCategoryDTO.setCategoryId(categoryId);
            Category changedCategory = modelMapper.map(changedCategoryDTO, Category.class);
            Category updatedCategory = categoryRepository.save(changedCategory);

            return modelMapper.map(updatedCategory, CategoryDTO.class);
        }

        throw new ResourceNotFoundException("Category", "categoryId", categoryId);

    }


}
