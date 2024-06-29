package com.foodBudy_v2.demo.payload;

import jakarta.validation.constraints.*;

public class ProductDTO {
    private Long productId;
    @NotNull
    @NotBlank
    @Size(max = 30)
    private String productName;

    private String image;

    @Size(max = 255)
    private String description;

    @NotNull
    @Min(0)
    @Max(10000)
    private Double originalPrice;

    @NotNull
    @Min(0)
    @Max(10000)
    private Double discountedPrice;

    private Integer discountPercentage;

    private Integer quantity;


    //TODO: validate the pattern
    @NotNull
    private String valid_until;

}
