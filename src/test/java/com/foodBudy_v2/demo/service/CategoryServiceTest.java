package com.foodBudy_v2.demo.service;
import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.exception.ResourceNotFoundException;
import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.payload.CategoryDTO;
import com.foodBudy_v2.demo.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    ModelMapper modelMapper=new ModelMapper();
    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    private CategoryDTO categoryDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, modelMapper);

        categoryDTO = CategoryDTO.builder()
                .categoryName("category1")
                .image("default.png")
                .build();

        category = Category.builder()
                .categoryId(1L)
                .categoryName("category1")
                .image("default.png")
                .build();

    }



    @Test
    void categoryService_createCategory_returnsCategoryDTO(){
        // Arrange
        when(categoryRepository.findByCategoryNameIgnoreCase(categoryDTO.getCategoryName()))
                .thenReturn(Optional.empty());
        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);

        // Act
        CategoryDTO createdCategoryDTO = categoryService.createCategory(categoryDTO);

        // Assert
        assertNotNull(createdCategoryDTO);
        assertEquals("category1", createdCategoryDTO.getCategoryName());


        verify(categoryRepository, times(1)).findByCategoryNameIgnoreCase(categoryDTO.getCategoryName());
        verify(categoryRepository, times(1)).save(category);

        // assert findByCategoryNameIgnoreCase was called with exactly the category name
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(categoryRepository).findByCategoryNameIgnoreCase(stringArgumentCaptor.capture());
        String capturedString = stringArgumentCaptor.getValue();
        assertThat(capturedString).isEqualTo(categoryDTO.getCategoryName());



    }

    @Test
    void categoryService_createCategory_categoryAlreadyExists() {
        // Arrange
        when(categoryRepository.findByCategoryNameIgnoreCase(categoryDTO.getCategoryName()))
                .thenReturn(Optional.of(category));

        String expectedMessage = "Category with the name " + categoryDTO.getCategoryName() + " already exists!";

        // Act & Assert
        assertThatThrownBy(()->categoryService.createCategory(categoryDTO))
                .isInstanceOf(APIException.class)
                .hasMessageContaining(expectedMessage);

        verify(categoryRepository, times(1)).findByCategoryNameIgnoreCase(categoryDTO.getCategoryName());
        verify(categoryRepository, never()).save(any(Category.class));
    }



    @Test
    void categoryService_deleteCategory_returnsCategoryDTO(){
        Long categoryId = 1L;

        // ARRANGE mocks
        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.of(category));

        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);

        // ACT
        CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);

        // ASSERT
        assertThat(deletedCategoryDTO).isNotNull();
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).delete(category);

    }

    @Test
    void categoryService_deleteCategory_categoryNotFound(){
        Long categoryId = 1L;

        // ARRANGE mocks
        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        String expectedMessage = "Category not found with categoryId: "+ categoryId;

        // Act & Assert
        assertThatThrownBy(()->categoryService.deleteCategory(categoryId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(expectedMessage);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).delete(any(Category.class));
    }





}











