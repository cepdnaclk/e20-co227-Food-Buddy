package com.foodBudy_v2.demo.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response object of products")
public class ProductResponse {

    @Schema(description = "List of categories returned")
    private List<ProductDTO> content;

    @Schema(description = "Current page number", example = "1")
    private Integer pageNumber;

    @Schema(description = "Size of the page" , example = "40")
    private Integer pageSize;

    @Schema(description = "No. of elements in the Page" , example = "10")
    private Long totalElements;

    @Schema(description = "Total Number of pages" , example = "10")
    private Integer totalPages;

    @Schema(description = "Indication(true or false) whether this is the last page" , example = "true")
    private boolean lastPage;
}
