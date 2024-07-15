package com.foodBudy_v2.demo.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodBudy_v2.demo.model.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime valid_until;

    private Long categoryId;

    private Long shopId;
}
