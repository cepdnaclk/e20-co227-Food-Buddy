package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.model.AppRole;
import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.model.Role;
import com.foodBudy_v2.demo.model.Shop;
import com.foodBudy_v2.demo.payload.ShopDTO;
import com.foodBudy_v2.demo.repository.AddressRepository;
import com.foodBudy_v2.demo.repository.RoleRepository;
import com.foodBudy_v2.demo.repository.ShopRepository;
import com.foodBudy_v2.demo.repository.UserRepository;
import com.foodBudy_v2.demo.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    private void updateUserRoles(AppUser user) {
        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                .orElseGet(() -> {
                    Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                    return roleRepository.save(newSellerRole);
                });

        user.getRoles().add(sellerRole);
    }
}
