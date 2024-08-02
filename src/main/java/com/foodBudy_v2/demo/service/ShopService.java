package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.payload.ShopDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ShopService {
    ShopDTO createShop(ShopDTO shopDTO);

    ShopDTO getShop();

    List<ShopDTO> getAllShops();

    List<ShopDTO> getAllActiveShops();

    ShopDTO updateShop(ShopDTO shopDTO);

    void deleteShop();

    ShopDTO getShopById(Long shopId);

    ShopDTO updateShopImage(Long shopId, MultipartFile image) throws IOException;


    byte[] downloadShopImage(String fileName) throws IOException;
}
