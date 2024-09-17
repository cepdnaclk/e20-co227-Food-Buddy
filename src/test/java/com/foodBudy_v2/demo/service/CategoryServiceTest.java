package com.foodBudy_v2.demo.service;
import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.exception.ResourceNotFoundException;
import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.payload.CategoryDTO;
import com.foodBudy_v2.demo.payload.CategoryResponse;
import com.foodBudy_v2.demo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    ModelMapper modelMapper=new ModelMapper();
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FileService fileService;

    private CategoryService categoryService;

    private CategoryDTO categoryDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, modelMapper, fileService);

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

    @Nested
    @DisplayName("Tests for createCategory")
    class CreateCategoryTests {
        @Test
        void createCategory_validCategoryName_returns_categoryDTO(){
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
        void createCategory_alreadyExistingCategoryName_throws_APIException() {
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
    }


    @Nested
    @DisplayName("Tests for deleteCategory")
    class DeleteCategoryTests {
        @Test
        void deleteCategory_categoryFound_returns_CategoryDTO(){
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
        void deleteCategory_categoryNotFound_throws_ResourceNotFoundException(){
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

    @Nested
    @DisplayName("Tests for updateCategory")
    class UpdateCategoryTests {
        private CategoryDTO categoryDTOInput;
        private Category updatedCategory;
        private CategoryDTO categoryDTOOutput;

        @BeforeEach
        void setUp() {

            categoryDTOInput = CategoryDTO.builder()
                    .categoryName("category1-updated")
                    .image("default.png")
                    .build();

            updatedCategory = Category.builder()
                    .categoryId(1L)
                    .categoryName("category1-updated")
                    .image("default.png")
                    .build();

            categoryDTOOutput = CategoryDTO.builder()
                    .categoryName("category1-updated")
                    .image("default.png")
                    .build();

        }

        @Test
        void updateCategory_categoryFound_returns_updatedCategoryDTO() {
            Long categoryId = 1L;

            // ARRANGE mocks**********************************************
            when(categoryRepository.findById(anyLong()))
                    .thenReturn(Optional.of(category));

            when(categoryRepository.save(category)).thenReturn(updatedCategory);

            when(modelMapper.map(updatedCategory, CategoryDTO.class)).thenReturn(categoryDTOOutput);

            ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);



            // ACT**********************************************
            CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTOInput, categoryId);

            // ASSERT**********************************************
            assertThat(updatedCategoryDTO).isNotNull();
            verify(categoryRepository, times(1)).findById(categoryId);
            verify(categoryRepository, times(1)).save(categoryArgumentCaptor.capture());
            verify(modelMapper, times(1)).map(updatedCategory,CategoryDTO.class);

            // assert save was called with the properly updated category object
            Category capturedCategory = categoryArgumentCaptor.getValue();
            // Check if all the properties are correctly updated
            assertThat(capturedCategory.getCategoryName()).isEqualTo(categoryDTOInput.getCategoryName());
            assertThat(capturedCategory.getImage()).isEqualTo(categoryDTOInput.getImage());


            // Optionally, check the returned DTO
            assertThat(updatedCategoryDTO).isEqualTo(categoryDTOOutput);
        }

        @Test
        void updateCategory_categoryNotFound_throws_ResourceNotFoundException() {
            Long categoryId = 1L;

            // ARRANGE mocks
            when(categoryRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            String expectedMessage = "Category not found with categoryId: "+ categoryId;

            // Act & Assert
            assertThatThrownBy(()->categoryService.updateCategory(categoryDTOInput, categoryId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining(expectedMessage);

            verify(categoryRepository, times(1)).findById(categoryId);
            verify(categoryRepository, never()).save(any(Category.class));
            verify(modelMapper, never()).map(any(Category.class),eq(CategoryDTO.class));
        }

    }


    @Nested
    @DisplayName("Tests for updateCategoryImage")
    class UpdateCategoryImageTests {
        private CategoryDTO updatedCategoryDTO;
        private Category updatedCategory;
        private MultipartFile mockFile;

        @Value("${category.image.path}")
        private String path;

        @BeforeEach
        void setUp() {
            updatedCategory = Category.builder()
                    .categoryId(1L)
                    .categoryName("category1")
                    .image("new-name.png")
                    .build();

            updatedCategoryDTO = CategoryDTO.builder()
                    .categoryName("category1")
                    .image("new-name.png")
                    .build();

            mockFile = new MockMultipartFile("image", "image.png", "image/png", "image-data".getBytes());




        }
        @Test
        void updateCategoryImage_categoryExists_returns_updatedCategoryDTO() throws IOException {
            // Arrange**********************************************
            Long categoryId = 1L;

            when(categoryRepository.findById(anyLong()))
                    .thenReturn(Optional.of(category));

            when(fileService.uploadImage(any(), any(MultipartFile.class)))
                    .thenReturn(updatedCategory.getImage());

            when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

            when(modelMapper.map(updatedCategory, CategoryDTO.class)).thenReturn(updatedCategoryDTO);

            ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);


            // Act**********************************************
            CategoryDTO outputCategoryDTO = categoryService.updateCategoryImage(categoryId, mockFile);



            // Assert**********************************************
            assertThat(outputCategoryDTO).isNotNull();
            verify(categoryRepository, times(1)).findById(categoryId);
            verify(fileService, times(1)).uploadImage(path, mockFile);
            verify(categoryRepository, times(1)).save(categoryArgumentCaptor.capture());
            verify(modelMapper, times(1)).map(any(Category.class),eq(CategoryDTO.class));


            // assert save was called with the properly updated category object
            Category capturedCategory = categoryArgumentCaptor.getValue();
            // Check if all the properties are correctly updated(image name)
            assertThat(capturedCategory.getImage()).isEqualTo(updatedCategory.getImage());
        }

        @Test
        void updateCategoryImage_categoryNotFound_throws_ResourceNotFoundException() {
            Long categoryId = 1L;

            // ARRANGE mocks
            when(categoryRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            String expectedMessage = "Category not found with categoryId: "+ categoryId;

            // Act & Assert
            assertThatThrownBy(()->categoryService.updateCategoryImage(categoryId, mockFile))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining(expectedMessage);

            verify(categoryRepository, times(1)).findById(categoryId);
            verify(categoryRepository, never()).save(any(Category.class));
            verify(modelMapper, never()).map(any(Category.class),eq(CategoryDTO.class));
        }
    }





    @Nested
    @DisplayName("Tests for getAllCategories")
    class GetAllCategoriesTests {
        private List<Category> categories;

        private Category category1;
        private Category category2;

        private CategoryDTO categoryDTO1;
        private CategoryDTO categoryDTO2;
        @BeforeEach
        void setUp() {

            category1 = Category.builder()
                    .categoryId(1L)
                    .categoryName("category1")
                    .image("default.png")
                    .build();

            category2 = Category.builder()
                    .categoryId(2L)
                    .categoryName("category2")
                    .image("default.png")
                    .build();

            categoryDTO1 = CategoryDTO.builder()
                    .categoryName("category1")
                    .image("default.png")
                    .build();

            categoryDTO1 = CategoryDTO.builder()
                    .categoryName("category2")
                    .image("default.png")
                    .build();

            categories = new ArrayList<>();

            categories.add(category1);
            categories.add(category2);
        }

        @Test
        void getAllCategories_returns_categoryResponse() {
            // ARRANGE mocks**********************************************
            when(categoryRepository.findAll())
                    .thenReturn(categories);

            when(modelMapper.map(category1, CategoryDTO.class)).thenReturn(categoryDTO1);

            when(modelMapper.map(category2, CategoryDTO.class)).thenReturn(categoryDTO2);

            // ACT**********************************************
            CategoryResponse categoryResponse = categoryService.getAllCategories();

            // ASSERT**********************************************
            assertThat(categoryResponse).isNotNull();
            assertThat(categoryResponse.getContent().size()).isEqualTo(2);

            verify(categoryRepository, times(1)).findAll();
            verify(modelMapper, times(1)).map(category1, CategoryDTO.class);
            verify(modelMapper, times(1)).map(category2, CategoryDTO.class);
        }
    }


    @Nested
    @DisplayName("Tests for downloadProductImage")
    class DownloadProductImageTests {

        private String fileName;
        private byte[] fileData;

        @Value("${category.image.path}")
        private String path;

        @BeforeEach
        void setUp() {
            fileName = "product-image.png";
            fileData = "image-data".getBytes();  // Dummy byte array for image
        }
        @Test
        void downloadProductImage_validFileName_returns_byteArray() throws IOException {
            // Arrange
            when(fileService.downloadPhotoFromFileSystem(path, fileName))
                    .thenReturn(fileData);

            // Act
            byte[] result = categoryService.downloadProductImage(fileName);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(fileData);  // Check that the returned byte array matches

            // Verify that the downloadPhotoFromFileSystem method was called with the correct parameters
            verify(fileService, times(1)).downloadPhotoFromFileSystem(eq(path), eq(fileName));

        }

    }

}











