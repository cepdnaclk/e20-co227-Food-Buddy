package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.payload.ShopDTO;

import java.util.List;

public interface ShopService {
    ShopDTO createShop(ShopDTO shopDTO);

    ShopDTO getShop();

    List<ShopDTO> getAllShops();

    ShopDTO updateShop(ShopDTO shopDTO);

    void deleteShop();
}
