package com.foodBudy_v2.demo.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long categoryId;

    @NotBlank
    @Size(min = 3, max = 15)
    private String categoryName;

    //TODO-categoryImage
    private String image;
}
