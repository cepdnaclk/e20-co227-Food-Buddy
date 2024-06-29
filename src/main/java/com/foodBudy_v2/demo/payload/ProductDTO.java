package com.foodBudy_v2.demo.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodBudy_v2.demo.model.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonIgnore
    private Category category;

}
