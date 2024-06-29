package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.exception.ResourceNotFoundException;
import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.model.Product;
import com.foodBudy_v2.demo.model.Shop;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.repository.CategoryRepository;
import com.foodBudy_v2.demo.repository.ProductRepository;
import com.foodBudy_v2.demo.repository.ShopRepository;
import com.foodBudy_v2.demo.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    private FileService fileService;
    private ShopRepository shopRepository;

    private AuthUtil authUtil;

    @Value("${product.image}")
    private String path;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, FileService fileService, ShopRepository shopRepository, AuthUtil authUtil) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.shopRepository = shopRepository;
        this.authUtil = authUtil;
    }


    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        //Shop shop = authUtil.loggedInUser().getShop();
        Shop shop = shopRepository.findByOwner(authUtil.loggedInUser())
                .orElseThrow(() -> new APIException("user does not have a shop"));


        // get the category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        Product product = convertDTOtoProduct(productDTO, category, shop);

        productRepository.save(product);
        shop.getProducts().add(product);
        shopRepository.save(shop);

        return modelMapper.map(product, ProductDTO.class);

    }

    private Product convertDTOtoProduct(ProductDTO productDTO, Category category, Shop shop) {
        Product product = new Product();

        product.setProductName(productDTO.getProductName());
        product.setImage("default.png");
        product.setDescription(productDTO.getDescription());
        product.setOriginalPrice(productDTO.getOriginalPrice());
        product.setDiscountedPrice(productDTO.getDiscountedPrice());
        product.setDiscountPercentage(
                (int)((productDTO.getOriginalPrice() - productDTO.getDiscountedPrice())/ productDTO.getOriginalPrice() * 100)
        );

        product.setQuantity(productDTO.getQuantity());

        product.setShop(shop);

        product.setCategory(category);

        // Get validUntil as a timestamp
        //TODO:validate/exception
        Timestamp validUntil = Timestamp.from(Instant.parse(productDTO.getValid_until()));
        product.setValid_until(validUntil);

        return product;
    }
}
