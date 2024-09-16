package com.foodBudy_v2.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Schema(description = "Category Entity")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the category" , example = "12")
    private Long categoryId;

    @Column(nullable = false, length = 20)
    @Schema(description = "Category name" , example = "Chinese")
    private String categoryName;

    @Schema(description = "A image of the category" )
    @Column(nullable = false)
    private String image;

    @Schema(description = "Products that belongs to the category" , example = "[\"Fried Rice\", \"Sushi\"]")
    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

}
