package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.exception.ResourceNotFoundException;
import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.model.Product;
import com.foodBudy_v2.demo.model.Shop;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.payload.ProductResponse;
import com.foodBudy_v2.demo.repository.CategoryRepository;
import com.foodBudy_v2.demo.repository.ProductRepository;
import com.foodBudy_v2.demo.repository.ShopRepository;
import com.foodBudy_v2.demo.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    private FileService fileService;
    private ShopRepository shopRepository;
    private AuthUtil authUtil;

    private GeoService geoService;

    @Value("${product.image}")
    private String path;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, FileService fileService, ShopRepository shopRepository, AuthUtil authUtil, GeoService geoService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.shopRepository = shopRepository;
        this.authUtil = authUtil;
        this.geoService = geoService;
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

        //Product product = convertDTOtoProduct(productDTO, category, shop);
        Product product = modelMapper.map(productDTO, Product.class);


        // set the values which are not added by the mapper
        product.setDiscountPercentage(
                (int)((productDTO.getOriginalPrice() - productDTO.getDiscountedPrice())/ productDTO.getOriginalPrice() * 100)
        );
        product.setShop(shop);
        product.setCategory(category);

        productRepository.save(product);


        shop.getProducts().add(product);
        shopRepository.save(shop);

        return modelMapper.map(product, ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create a Pageable object by including the page details
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        // call findAll by passing the Pageable as an argument
        // return value is type Page
        Page<Product> productPage = productRepository.findAll(pageDetails);

        // get the list of categories from the category page
        List<Product> products = productPage.getContent();

        ProductResponse productResponse = createProductResponse(products, productPage);

        return productResponse;

    }



    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // get the category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        //pagination
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create a Pageable object by including the page details
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        // call findAll by passing the Pageable as an argument
        // return value is type Page
        Page<Product> productPage = productRepository.findByCategoryOrderByDiscountedPriceAsc(pageDetails, category);

        // get the list of products from the product page
        List<Product> products = productPage.getContent();

        if (products.isEmpty()){
            throw new APIException(category.getCategoryName() + " category does not have any products");
        }


        ProductResponse productResponse = createProductResponse(products, productPage);

        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //pagination
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create a Pageable object by including the page details
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository
                .findByProductNameLikeIgnoreCaseOrderByDiscountedPriceAsc("%" + keyword + "%", pageDetails);

        // get the list of products from the product page
        List<Product> products = productPage.getContent();

        if (products.isEmpty()){
            throw new APIException("Products not found with keyword: "+ keyword);
        }

        ProductResponse productResponse = createProductResponse(products, productPage);

        return productResponse;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // get the product from DB
        Product dbProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // upload image to the server
        // get the filename of updated image
        String fileName = fileService.uploadImage(path, image);

        // update the new file name to the product
        dbProduct.setImage(fileName);

        // save the product
        Product updatedProduct = productRepository.save(dbProduct);

        // return DTO after mapping to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponse getNearbyProducts(double userLatitude, double userLongitude, double radius) {
        // Get all the products from database
        List<Product> products = productRepository.findAll();

        List<Product> nearbyProducts = new ArrayList<>();

        for (Product product : products){

            Shop shop = product.getShop();

            double shopLatitude = shop.getLatitude();
            double shopLongitude = shop.getLongitude();

            double distance = geoService.calculateDistance(
                    userLatitude, userLongitude,
                    shopLatitude, shopLongitude
            );

            if (distance <= radius){
                nearbyProducts.add(product);
            }
        }
        List<ProductDTO> nearbyProductDTOS = nearbyProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(nearbyProductDTOS);

        return productResponse;
    }


    private ProductResponse createProductResponse(List<Product> products, Page<Product> productPage) {
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }
}
