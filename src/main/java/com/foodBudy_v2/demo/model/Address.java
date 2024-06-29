package com.foodBudy_v2.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(nullable = false)
    private String addressLine1;

    @Column(nullable = false)
    private String addressLine3;

    @Column(nullable = false)
    private String addressLine2;

//    @JsonIgnore
//    @OneToOne(mappedBy = "address")
//    private Shop shop;

}
