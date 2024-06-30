package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.config.AppConfig;
import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.payload.CategoryDTO;
import com.foodBudy_v2.demo.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    ModelMapper modelMapper=new ModelMapper();

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void categoryService_createCategory_returnsCategoryDTO(){
        Category category = Category.builder()
                .categoryName("category1")
                .image("default.png")
                .build();

        CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryName("category1")
                .image("default.png")
                .build();

        // fake the method
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);

        Assertions.assertThat(savedCategoryDTO).isNotNull();


    }

}