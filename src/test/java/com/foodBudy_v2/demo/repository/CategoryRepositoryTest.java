package com.foodBudy_v2.demo.repository;

import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.model.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CategoryRepositoryTest {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryRepositoryTest(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Test
    public void categoryRepository_save_returnSavedCategory(){

        //Arrange
        Category category = Category.builder()
                .categoryName("Rice & curry")
                .image("categoryImage.png")
                .build();

        //Act
        Category savedCategory = categoryRepository.save(category);

        //Assert
        Assertions.assertThat(savedCategory).isNotNull();
        Assertions.assertThat(savedCategory.getCategoryId()).isGreaterThan(0);
    }

    @Test
    public void categoryRepository_findAll_returnAllCategory(){
        //Arrange
        Category category1 = Category.builder()
                .categoryName("Rice & Curry")
                .image("categoryImage.png")
                .build();

        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .categoryName("Rice & Curry")
                .image("categoryImage.png")
                .build();

        categoryRepository.save(category2);

        Category category3 = Category.builder()
                .categoryName("Rice & Curry")
                .image("categoryImage.png")
                .build();

        categoryRepository.save(category3);

        // Act
        List<Category> categories = categoryRepository.findAll();


        // assert
        Assertions.assertThat(categories.size()).isEqualTo(3);


    }

    @Test
    public void categoryRepository_delete_deleteTheCategory(){
        //Arrange
        Category category1 = Category.builder()
                .categoryName("Rice & Curry")
                .image("categoryImage.png")
                .build();

        categoryRepository.save(category1);
        Long category1Id = category1.getCategoryId();

        //Act
        categoryRepository.delete(category1);
        Optional<Category> OpCategory = categoryRepository.findById(category1Id);

        //Assert
        Assertions.assertThat(OpCategory.isEmpty()).isTrue();

    }

    @Test
    public void categoryRepository_findById_returnFoundCategory(){
        //Arrange
        Category category1 = Category.builder()
                .categoryName("Rice & Curry")
                .image("categoryImage.png")
                .build();

        categoryRepository.save(category1);
        Long category1Id = category1.getCategoryId();

        //Act
        Optional<Category> OpCategory = categoryRepository.findById(category1Id);

        //Assert
        Assertions.assertThat(OpCategory.isPresent()).isTrue();
    }

    @Test
    public void categoryRepository_findByCategoryNameIgnoreCase_returnFoundCategory(){

        //Arrange
        Category category = Category.builder()
                .categoryName("Rice & Curry")
                .image("categoryImage.png")
                .build();

        categoryRepository.save(category);

        //Act
        Category foundCategory = categoryRepository.findByCategoryNameIgnoreCase("rice & curry").get();

        //Assert
        Assertions.assertThat(foundCategory).isNotNull();
        Assertions.assertThat(foundCategory.getImage().equals("categoryImage.png")).isTrue();
    }
}