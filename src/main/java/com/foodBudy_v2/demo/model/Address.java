package com.foodBudy_v2.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Address Entity")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique Identifier of the Address", example = "0")
    private Long addressId;

    @Schema(description = "Address Line 1. Cannot be Null and Length should not exceed 80.")
    @Column(nullable = false, length =80)
    private String addressLine1;

    @Schema(description = "Address Line 2. Cannot be Null and Length should not exceed 80.")
    @Column(nullable = false, length =80)
    private String addressLine2;

    @Schema(description = "Address Line 3. Cannot be Null and Length should not exceed 80.")
    @Column(nullable = false, length =80)
    private String addressLine3;

//    @JsonIgnore
//    @OneToOne(mappedBy = "address")
//    private Shop shop;

}
