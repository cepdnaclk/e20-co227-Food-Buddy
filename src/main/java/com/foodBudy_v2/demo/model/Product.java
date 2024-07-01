package com.foodBudy_v2.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 30)
    private String productName;

    private String image;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double originalPrice;

    @Column(nullable = false)
    private Double discountedPrice;

    @Column(nullable = false)
    private Integer discountPercentage;


    private Integer quantity;

    @Column(nullable = false)
    private Timestamp valid_until;



    @ManyToOne(optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    public void setValid_until(String valid_until) {
        this.valid_until = Timestamp.from(Instant.parse(valid_until));;
    }

//    public void setValid_until(Timestamp valid_until) {
//        this.valid_until = valid_until;
//    }
}
