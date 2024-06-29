package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.model.Shop;
import com.foodBudy_v2.demo.payload.ShopDTO;
import com.foodBudy_v2.demo.service.ShopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
