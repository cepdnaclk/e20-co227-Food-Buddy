package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.model.Product;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTests {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private static RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
        baseUrl  = "http://localhost:" + port + "/api/";
    }

    @Test
    void testAddProduct() {
        ProductDTO productDTO = ProductDTO.builder()
                .productName("Test Product")
                .description("Test Description")
                .originalPrice(100.0)
                .discountedPrice(90.0)
                .discountPercentage(10)
                .quantity(100)
                .valid_until(LocalDateTime.now().plusDays(10))
                .shopId(1L)
                .categoryId(1L)
                .build();
    }

}
