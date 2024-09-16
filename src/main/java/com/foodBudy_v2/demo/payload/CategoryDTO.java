package com.foodBudy_v2.demo.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for Category")
public class CategoryDTO {

    @Schema(description = "Unique identifier of the category" , example = "2")
    private Long categoryId;

    @NotBlank
    @Size(min = 3, max = 15)
    @Schema(description = "Name of the category" , example = "Chinese")
    private String categoryName;

    //TODO-categoryImage
    @Schema(description = "Image URL for the category" , example = "egg-rice-and-curry.png")
    private String image;
}
