package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.model.Shop;
import com.foodBudy_v2.demo.payload.APIResponse;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.payload.ShopDTO;
import com.foodBudy_v2.demo.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ShopController {

    private ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // create a shop
    @PostMapping("/shops")
    @Operation(summary = "Create a new shop", description = "This endpoint allows a user to create a shop" ,
            requestBody = @RequestBody(description = "ShopDTO that needs to be created" , required = true))
    public ResponseEntity<ShopDTO> createShop(@Valid @RequestBody ShopDTO shopDTO){

        ShopDTO savedShodDTO = shopService.createShop(shopDTO);

        return  new ResponseEntity<>(savedShodDTO, HttpStatus.CREATED);
    }

    // get my shop
    @GetMapping("/shops/myShop")
    @Operation(summary = "Get my shop " , description = "Get the details of the shop which was created by the user")
    public ResponseEntity<ShopDTO> getShop(){
        ShopDTO myShopDTO = shopService.getShop();
        return  new ResponseEntity<>(myShopDTO, HttpStatus.OK);
    }

    // get shop by shopId
    @GetMapping ("public/shops/{shopId}")
    @Operation(summary = "Get shop by ID" , description = "Retrieve a shop's details by its ID")
    @Parameter(name = "shopId" , description = "Unique identifier of the shop" , required = true , example = "2")
    public ResponseEntity<ShopDTO> getShopById(@PathVariable Long shopId){
        ShopDTO shopDTO = shopService.getShopById(shopId);
        return new ResponseEntity<>(shopDTO, HttpStatus.OK);
    }

    // get all shop
    @GetMapping("/public/shops")
    @Operation(summary = "Get all shops" , description = "Retrive details of all the shops")
    public ResponseEntity<List<ShopDTO>> getAllShops(){
        List<ShopDTO> shopDTOs = shopService.getAllShops();
        return  new ResponseEntity<>(shopDTOs, HttpStatus.OK);
    }
    // get all active shop
    @GetMapping("/public/shops/active")
    @Operation(summary = "Get all active shops" , description = "Retrive details of all shops which have at least one product")
    public ResponseEntity<List<ShopDTO>> getAllActiveShops(){
        List<ShopDTO> shopDTOs = shopService.getAllActiveShops();
        return  new ResponseEntity<>(shopDTOs, HttpStatus.OK);
    }

    // update shop
    @PutMapping("/shops")
    @Operation(summary = "Update shop" , description = "Update Shop details")
    public ResponseEntity<ShopDTO> updateShop(@Valid @RequestBody ShopDTO shopDTO){

        ShopDTO updatedShodDTO = shopService.updateShop(shopDTO);

        return  new ResponseEntity<>(updatedShodDTO, HttpStatus.OK);
    }

    @DeleteMapping("/shops")
    @Operation(summary = "Delete shop" , description = "Delete shop ")
    public ResponseEntity<APIResponse> deleteShop(){
        shopService.deleteShop();
        return new ResponseEntity<>(new APIResponse("Deleted successfully", true), HttpStatus.OK);
    }


    // update the shop image
    @PutMapping("/shops/{shopId}/image")
    @Operation(summary = "Update shop image" , description = "Update the image of the shop")
    public ResponseEntity<ShopDTO> updateShopImage(
            @PathVariable Long shopId, @RequestParam("image") MultipartFile image)
            throws IOException {

        ShopDTO updatedShopDTO =  shopService.updateShopImage(shopId, image);

        return new ResponseEntity<>(updatedShopDTO, HttpStatus.OK);
    }


    // download the shop image
    @GetMapping("/public/shops/images/{fileName}")
    @Operation(summary = "Get the shop image" , description = "Retrieve the image of a shop based on the shop name")
    @Parameter(name = "fileName" , description = "The shop image name" , required = true , example = "kapila-bakers")
    public ResponseEntity<?> getShopImage(@PathVariable String fileName) throws IOException {

        byte [] photoData = shopService.downloadShopImage(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(photoData);

    }







}
