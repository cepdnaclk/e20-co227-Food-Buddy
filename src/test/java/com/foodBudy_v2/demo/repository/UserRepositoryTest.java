package com.foodBudy_v2.demo.repository;

import com.foodBudy_v2.demo.model.AppUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
    @Test
    public void userRepository_findByUsername_returnsUserIfPresent(){

        //Arrange
        AppUser user = AppUser.builder()
                .email("test@testmail.com")
                .password("test123")
                .username("test")
                .build();

        //Act
        userRepository.save(user);
        Optional<AppUser> appUser = userRepository.findByUsername("test");
        Optional<AppUser> appUser1 = userRepository.findByUsername("test1");


        //Assert
        Assertions.assertThat(appUser.isPresent()).isTrue();
        Assertions.assertThat(appUser1.isPresent()).isFalse();
    }

    @Test
    public void userRepository_existsByUsername_returnsTrueIfExists(){

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
    public void userRepository_existsByEmail_returnsTrueIfExist(){

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