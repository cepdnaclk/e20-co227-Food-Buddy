package com.foodBudy_v2.demo.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @Column(nullable = false, unique = true, length = 30)
    private String shopName;

    private String image;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;



    @OneToOne
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

//    public void setOwner(AppUser owner) {
//        this.owner = owner;
//        owner.setShop(this);
//    }
}
