package com.foodBudy_v2.demo.repository;

import com.foodBudy_v2.demo.model.AppUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {


    private UserRepository userRepository;

    //@Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void userRepository_save_returnSavedUser(){

        //Arrange
        AppUser user = AppUser.builder()
                .email("test@testmail.com")
                .password("test123")
                .username("test")
                .build();

        //Act
        AppUser savedUser = userRepository.save(user);

        //Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUserId()).isGreaterThan(0);
    }

    @Test
    public void userRepository_findByUsername_returnUser(){

        //Arrange
        AppUser user = AppUser.builder()
                .email("test@testmail.com")
                .password("test123")
                .username("test")
                .build();

        //Act
        userRepository.save(user);
        AppUser appUser = userRepository.findByUsername("test").get();

        //Assert
        Assertions.assertThat(appUser).isNotNull();
    }

    @Test
    public void userRepository_existsByUsername_returnTrue(){

        //Arrange
        AppUser user = AppUser.builder()
                .email("test@testmail.com")
                .password("test123")
                .username("test")
                .build();

        //Act
        userRepository.save(user);
        Boolean isPresent = userRepository.existsByUsername("test");

        //Assert
        Assertions.assertThat(isPresent).isTrue();
    }

    @Test
    public void userRepository_existsByEmail_returnTrue(){

        //Arrange
        AppUser user = AppUser.builder()
                .email("test@testmail.com")
                .password("test123")
                .username("test")
                .build();

        //Act
        userRepository.save(user);
        Boolean isPresent = userRepository.existsByEmail(user.getEmail());

        //Assert
        Assertions.assertThat(isPresent).isTrue();
    }
}