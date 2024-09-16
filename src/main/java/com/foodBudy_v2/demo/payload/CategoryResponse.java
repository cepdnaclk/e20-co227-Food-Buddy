package com.foodBudy_v2.demo.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object that contains a list of categories")
public class CategoryResponse {

    @Schema(description = "List of categories returned")
    private List<CategoryDTO> content;
}
