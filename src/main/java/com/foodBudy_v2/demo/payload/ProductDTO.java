package com.foodBudy_v2.demo.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodBudy_v2.demo.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data Transfer Object of A product")
public class ProductDTO {

    @Schema(description = "Unique identifier of the product" , example = "2")
    private Long productId;

    @NotNull
    @NotBlank
    @Size(max = 30)
    @Schema(description = "Name of the product" , example = "Rice and curry")
    private String productName;

    @Schema(description = "Image URL for the image" , example = "egg-rice-and-curry.png")
    private String image;

    @Size(max = 255)
    @Schema(description = "Description about the product" , example = "Plain old rice")
    private String description;

    @NotNull
    @Min(0)
    @Max(10000)
    @Schema(description = "Selling price of the product" , example = "450.0")
    private Double originalPrice;

    @NotNull
    @Min(0)
    @Max(10000)
    @Schema(description = "Price after the discount" , example = "300.0")
    private Double discountedPrice;

    @Schema(description = "Percentage of the discount" , example = "33")
    private Integer discountPercentage;

    @Schema(description = "Available quantity of the product" , example = "10")
    private Integer quantity;

    //TODO: validate the pattern
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Expiration date and time" , example = "2024-07-31T23:59:59")
    private LocalDateTime valid_until;

    @Schema(description = "Category ID of the product" , example = "1")
    private Long categoryId;

    @Schema(description = "Shop ID of the product" , example = "1")
    private Long shopId;

    @Schema(description = "Name of the shop this product is available at" , example = "kapila bakers")
    private String shopName;

    @Schema(description = "Contact information of the shop" , example = "07XXXXXXXX")
    private String shopPhoneNumber;
}
