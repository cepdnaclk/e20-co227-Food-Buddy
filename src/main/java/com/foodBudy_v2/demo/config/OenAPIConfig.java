package com.foodBudy_v2.demo.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OenAPIConfig {

    @Bean
    public OpenAPI foodBuddyAPI(){
        return new OpenAPI()
                .info(new Info().title("FoodBuddy API").description("Food Buddy is a solution for the food wastage of restaurants. We provide a platform, where the restaurants can display their unsold food items with attractive discounts and users can see those advertisements from nearby restaurants.").version("1.0"));
    }
}
