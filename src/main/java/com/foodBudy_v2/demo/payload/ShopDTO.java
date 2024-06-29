package com.foodBudy_v2.demo.payload;

import com.foodBudy_v2.demo.model.Address;
import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.model.Shop;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {

    private Long shopId;

    @NotNull
    @NotBlank
    @Size(max = 30)
    private String ShopName;

    @NotNull
    @DecimalMin(value = "-90", message = "Latitude must be greater than or equal to -90")
    @DecimalMax(value = "90", message = "Latitude must be less than or equal to 90")
    private Double latitude;

    @NotNull
    @DecimalMin(value = "-180", message = "Longitude must be greater than or equal to -180")
    @DecimalMax(value = "180", message = "Longitude must be less than or equal to 180")
    private Double longitude;

    @NotNull
    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must have exactly 10 digits")
    private String phoneNumber;

    private Address address;

    private Long ownerId;

}
