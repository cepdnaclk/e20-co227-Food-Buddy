package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.exception.ResourceNotFoundException;
import com.foodBudy_v2.demo.model.*;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.payload.ShopDTO;
import com.foodBudy_v2.demo.repository.AddressRepository;
import com.foodBudy_v2.demo.repository.RoleRepository;
import com.foodBudy_v2.demo.repository.ShopRepository;
import com.foodBudy_v2.demo.repository.UserRepository;
import com.foodBudy_v2.demo.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService{

    private ShopRepository shopRepository;
    private AuthUtil authUtil;

    private AddressRepository addressRepository;
    private ModelMapper modelMapper;

    private RoleRepository roleRepository;
    private UserRepository userRepository;



    public ShopServiceImpl(ShopRepository shopRepository, AuthUtil authUtil, AddressRepository addressRepository, ModelMapper modelMapper, RoleRepository roleRepository, UserRepository userRepository) {
        this.shopRepository = shopRepository;
        this.authUtil = authUtil;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ShopDTO createShop(ShopDTO shopDTO) {
        // get the authenticated user
        AppUser user = authUtil.loggedInUser();

        // check whether the user already has a shop
        if(shopRepository.existsByOwner(user)){
            throw new APIException("User already has a shop");
        }

        // check whether there is a shop with the same name
        if(shopRepository.existsByShopNameIgnoreCase(shopDTO.getShopName())){
            throw new APIException("Shop name is already taken");
        }

        Shop shop = modelMapper.map(shopDTO, Shop.class);

        shop.setOwner(user);

        shopRepository.save(shop);

        user.setShop(shop);
        updateUserRoles(user);
        userRepository.save(user);

        shopDTO.setShopId(shop.getShopId());
        shopDTO.setOwnerId(user.getUserId());
        return shopDTO;

    }

    @Override
    public ShopDTO getShop() {
        // get the authenticated user
        AppUser owner = authUtil.loggedInUser();

        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(() -> new APIException("Shop not found"));

        ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);

        return shopDTO;
    }

    @Override
    public List<ShopDTO> getAllShops() {
        List<Shop> shops = shopRepository.findAll();

        List<ShopDTO> shopDTOs = shops.stream()
                .map((shop)-> modelMapper.map(shop, ShopDTO.class)
                )
                .toList();

        return shopDTOs;
    }

    @Override
    public ShopDTO updateShop(ShopDTO shopDTO) {

        // get the authenticated user
        AppUser owner = authUtil.loggedInUser();

        // find the shop
//        Shop shop = shopRepository.findByOwner(owner)
////                .orElseThrow(()-> new APIException("Shop not found"));
        Shop shop = owner.getShop();

        if (!shop.getShopName().equals(shopDTO.getShopName())
        && shopRepository.existsByShopNameIgnoreCase(shopDTO.getShopName())){
            throw new APIException("Shop name is already taken");
        }

        if(!shop.getShopId().equals(shopDTO.getShopId())){
            throw new APIException("Invalid shopId");
        }

        shop.setShopName(shopDTO.getShopName());
        shop.setAddress(shopDTO.getAddress());
        shop.setLatitude(shopDTO.getLatitude());
        shop.setLongitude(shopDTO.getLongitude());
        shop.setPhoneNumber(shopDTO.getPhoneNumber());

        shopRepository.save(shop);

        ShopDTO updatedShopDTO = modelMapper.map(shop, ShopDTO.class);

        return updatedShopDTO;
    }

    @Override
    public void deleteShop() {
        // get the authenticated user
        AppUser owner = authUtil.loggedInUser();

        // find the shop
        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(()-> new APIException("Shop not found"));

        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                .orElseThrow(()-> new APIException("Role not found"));

        owner.getRoles().remove(sellerRole);
        owner.setShop(null);
        userRepository.save(owner);

        shopRepository.delete(shop);


    }

    @Override
    public ShopDTO getShopById(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(()-> new ResourceNotFoundException("Shop", "shopId", shopId));

        return modelMapper.map(shop, ShopDTO.class);
    }

    private void updateUserRoles(AppUser user) {
        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                .orElseGet(() -> {
                    Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                    return roleRepository.save(newSellerRole);
                });

        user.getRoles().add(sellerRole);
    }

    private void removeSellerRole(AppUser user) {
        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                        .orElseThrow(()-> new APIException("Role not found"));

        user.getRoles().remove(sellerRole);

        userRepository.save(user);
    }


}
