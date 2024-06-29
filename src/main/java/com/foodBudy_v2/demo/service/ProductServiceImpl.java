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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

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
