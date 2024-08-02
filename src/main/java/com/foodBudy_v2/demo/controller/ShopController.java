package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.model.Shop;
import com.foodBudy_v2.demo.payload.APIResponse;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.payload.ShopDTO;
import com.foodBudy_v2.demo.service.ShopService;
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
    public ResponseEntity<ShopDTO> createShop(@Valid @RequestBody ShopDTO shopDTO){

        ShopDTO savedShodDTO = shopService.createShop(shopDTO);

        return  new ResponseEntity<>(savedShodDTO, HttpStatus.CREATED);
    }

    // get my shop
    @GetMapping("/shops/myShop")
    public ResponseEntity<ShopDTO> getShop(){
        ShopDTO myShopDTO = shopService.getShop();
        return  new ResponseEntity<>(myShopDTO, HttpStatus.OK);
    }

    // get shop by shopId
    @GetMapping ("public/shops/{shopId}")
    public ResponseEntity<ShopDTO> getShopById(@PathVariable Long shopId){
        ShopDTO shopDTO = shopService.getShopById(shopId);
        return new ResponseEntity<>(shopDTO, HttpStatus.OK);
    }

    // get all shop
    @GetMapping("/public/shops")
    public ResponseEntity<List<ShopDTO>> getAllShops(){
        List<ShopDTO> shopDTOs = shopService.getAllShops();
        return  new ResponseEntity<>(shopDTOs, HttpStatus.OK);
    }
    // get all active shop
    @GetMapping("/public/shops/active")
    public ResponseEntity<List<ShopDTO>> getAllActiveShops(){
        List<ShopDTO> shopDTOs = shopService.getAllActiveShops();
        return  new ResponseEntity<>(shopDTOs, HttpStatus.OK);
    }

    // update shop
    @PutMapping("/shops")
    public ResponseEntity<ShopDTO> updateShop(@Valid @RequestBody ShopDTO shopDTO){

        ShopDTO updatedShodDTO = shopService.updateShop(shopDTO);

        return  new ResponseEntity<>(updatedShodDTO, HttpStatus.OK);
    }

    @DeleteMapping("/shops")
    public ResponseEntity<APIResponse> deleteShop(){
        shopService.deleteShop();
        return new ResponseEntity<>(new APIResponse("Deleted successfully", true), HttpStatus.OK);
    }


    // update the shop image
    @PutMapping("/shops/{shopId}/image")
    public ResponseEntity<ShopDTO> updateShopImage(
            @PathVariable Long shopId, @RequestParam("image") MultipartFile image)
            throws IOException {

        ShopDTO updatedShopDTO =  shopService.updateShopImage(shopId, image);

        return new ResponseEntity<>(updatedShopDTO, HttpStatus.OK);
    }


    // download the shop image
    @GetMapping("/public/shops/images/{fileName}")
    public ResponseEntity<?> getShopImage(@PathVariable String fileName) throws IOException {

        byte [] photoData = shopService.downloadShopImage(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(photoData);

    }







}
