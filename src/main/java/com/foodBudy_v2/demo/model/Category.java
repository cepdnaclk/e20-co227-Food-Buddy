package com.foodBudy_v2.demo.model;

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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, length = 20)
    private String categoryName;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

}
