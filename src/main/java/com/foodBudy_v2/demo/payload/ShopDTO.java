package com.foodBudy_v2.demo.payload;

import com.foodBudy_v2.demo.model.Address;
import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.model.Shop;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for a Shop")
public class ShopDTO {

    @Schema(description = "Unique identifier of the Shop" , example = "0")
    private Long shopId;

    @NotNull
    @NotBlank
    @Size(max = 30)
    @Schema(description = "Name of the Shop" , example = "Kapila Bakers")
    private String ShopName;

    @NotNull
    @DecimalMin(value = "-90", message = "Latitude must be greater than or equal to -90")
    @DecimalMax(value = "90", message = "Latitude must be less than or equal to 90")
    @Schema(description = "Latitude of the Shop location" , example = "7.267722191447389")
    private Double latitude;

    @NotNull
    @DecimalMin(value = "-180", message = "Longitude must be greater than or equal to -180")
    @DecimalMax(value = "180", message = "Longitude must be less than or equal to 180")
    @Schema(description = "Longitude of the Shop location" , example = "5.267722191447389")
    private Double longitude;

    @NotNull
    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must have exactly 10 digits")
    @Schema(description = "Phone number of the shop" , example = "0712556566")
    private String phoneNumber;

    @Schema(description = "Address of the shop" )
    private Address address;

    @Schema(description = "Unique identifier of the shop owner" , example = "5")
    private Long ownerId;

    @Schema(description = "Image URL for the Shop" , example = "kapila_bakers.png")
    private String image;
}
