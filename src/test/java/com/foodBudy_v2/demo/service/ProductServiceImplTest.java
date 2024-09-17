package com.foodBudy_v2.demo.service;

import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.exception.ResourceNotFoundException;
import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.model.Product;
import com.foodBudy_v2.demo.model.Shop;
import com.foodBudy_v2.demo.payload.CategoryDTO;
import com.foodBudy_v2.demo.payload.ProductDTO;
import com.foodBudy_v2.demo.repository.CategoryRepository;
import com.foodBudy_v2.demo.repository.ProductRepository;
import com.foodBudy_v2.demo.repository.ShopRepository;
import com.foodBudy_v2.demo.utils.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    ModelMapper modelMapper=new ModelMapper();
    @Mock
    private FileService fileService;
    @Mock
    private ShopRepository shopRepository;
    @Mock
    private AuthUtil authUtil;
    @Mock
    private GeoService geoService;

    private ProductService productService;

    private ProductDTO productDTOIn;
    private ProductDTO productDTOOut;
    private Product productInitial;
    private Product productFinal;

    private Category category;
    private Shop shop;

    private AppUser owner;

    private LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        localDateTime= LocalDateTime.now();

        productService = new ProductServiceImpl(productRepository, categoryRepository, modelMapper, fileService, shopRepository, authUtil, geoService);

        productDTOIn = ProductDTO.builder()
                .productId(null)
                .productName("product1")
                .image(null)
                .description("description")
                .originalPrice(100.0)
                .discountedPrice(90.0)
                .discountPercentage(null)
                .quantity(10)
                .valid_until(localDateTime)
                .categoryId(null)
                .shopId(null)
                .shopName(null)
                .shopPhoneNumber(null)
                .build();

        productDTOOut = ProductDTO.builder()
                .productId(1L)
                .productName("product1")
                .image("default.png")
                .description("description")
                .originalPrice(100.0)
                .discountedPrice(90.0)
                .discountPercentage(10)
                .quantity(10)
                .valid_until(localDateTime)
                .categoryId(1L)
                .shopId(1L)
                .shopName("shop1")
                .shopPhoneNumber("1234567890")
                .build();

        shop = new Shop();
        shop.setShopId(1L);

        category = new Category();
        category.setCategoryId(1L);

        productInitial = Product.builder()
                .productId(null)
                .productName("product1")
                .image("default.png")
                .description("description")
                .originalPrice(100.0)
                .discountedPrice(90.0)
                .discountPercentage(null)
                .quantity(10)
                .valid_until(localDateTime)
                .shop(null)
                .category(null)
                .build();

        productFinal = Product.builder()
                .productId(1L)
                .productName("product1")
                .image(null)
                .description("description")
                .originalPrice(100.0)
                .discountedPrice(90.0)
                .discountPercentage(10)
                .quantity(10)
                .valid_until(localDateTime)
                .shop(shop)
                .category(category)
                .build();


        owner = new AppUser();
        owner.setUserId(1L);


    }

    @Nested
    @DisplayName("Tests for addProduct")
    class AddProductTests {
        @Test
        void addProduct_validShop_validCategory_returns_productDTO() {
            // Arrange
            when(authUtil.loggedInUser()).thenReturn(owner);
            when(shopRepository.findByOwner(owner)).thenReturn(Optional.of(shop));
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
            when(modelMapper.map(productDTOIn, Product.class)).thenReturn(productInitial);

            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
                Product product = invocation.getArgument(0);
                product.setProductId(1L);  // Set the productId to a specific value, e.g., 123L
                return product;
            });

            ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);


            // Act
            ProductDTO resultProductDTO = productService.addProduct(productDTOIn, 1L);

            // Assert
            verify(authUtil, times(1)).loggedInUser();
            verify(shopRepository, times(1)).findByOwner(owner);
            verify(categoryRepository, times(1)).findById(anyLong());
            verify(modelMapper, times(1)).map(productDTOIn, Product.class);
            verify(productRepository, times(1)).save(productInitial);
            verify(shopRepository, times(1)).save(shop);
            verify(modelMapper, times(1)).map(productArgumentCaptor.capture(), eq(ProductDTO.class));

            // assert save was called with the properly updated category object
            Product capturedProduct = productArgumentCaptor.getValue();
            // Check if all the properties are correctly updated
            assertThat(capturedProduct)
                    .usingRecursiveComparison()
                    .isEqualTo(productFinal);

        }

        @Test
        void addProduct_categoryNotFound_throws_ResourceNotFoundException() {
            Long categoryId = 1L;

            // ARRANGE mocks
            when(authUtil.loggedInUser()).thenReturn(owner);
            when(shopRepository.findByOwner(owner)).thenReturn(Optional.of(shop));
            when(categoryRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            String expectedMessage = "Category not found with categoryId: "+ categoryId;

            // Act & Assert
            assertThatThrownBy(()->productService.addProduct(productDTOIn, categoryId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining(expectedMessage);

            verify(categoryRepository, times(1)).findById(categoryId);
            verify(productRepository, never()).save(any(Product.class));
            verify(shopRepository, never()).save(any(Shop.class));
            verify(modelMapper, never()).map(any(Product.class),eq(ProductDTO.class));

        }

        @Test
        void addProduct_ownerNotFound_throws_APIException() {
            Long categoryId = 1L;

            // ARRANGE mocks
            when(authUtil.loggedInUser()).thenReturn(null);


            String expectedMessage = "user does not have a shop";

            // Act & Assert
            assertThatThrownBy(()->productService.addProduct(productDTOIn, categoryId))
                    .isInstanceOf(APIException.class)
                    .hasMessageContaining(expectedMessage);

            verify(shopRepository, times(1)).findByOwner(any());
            verify(categoryRepository, never()).findById(any());
            verify(productRepository, never()).save(any(Product.class));
            verify(shopRepository, never()).save(any(Shop.class));
            verify(modelMapper, never()).map(any(Product.class),eq(ProductDTO.class));

        }



        @Test
        void addProduct() {
            // Arrange

            // Act

            // Assert
        }
    }

    @Nested
    @DisplayName("Tests for getAllProducts")
    class GetAllProductsTests {
        @Test
        void getAllProducts() {
            // Arrange

            // Act

            // Assert
        }
    }

    @Nested
    @DisplayName("Tests for searchByCategory")
    class SearchByCategoryTests {
        @Test
        void searchByCategory() {
            // Arrange

            // Act

            // Assert
        }
    }

    @Nested
    @DisplayName("Tests for updateProductImage")
    class UpdateProductImageTests {
        @Test
        void updateProductImage() {
            // Arrange

            // Act

            // Assert
        }
    }

    @Nested
    @DisplayName("Tests for downloadProductImage")
    class DownloadProductImageTests {
        @Test
        void downloadProductImage() {
            // Arrange

            // Act

            // Assert
        }
    }

    @Nested
    @DisplayName("Tests for getNearbyProducts")
    class GetNearbyProductsTests {
        @Test
        void getNearbyProducts() {
            // Arrange

            // Act

            // Assert
        }
    }

    @Nested
    @DisplayName("Tests for getProductsByShop")
    class GetProductsByShopTests {
        @Test
        void getProductsByShop() {
            // Arrange

            // Act

            // Assert
        }
    }

    @Nested
    @DisplayName("Tests for deleteProduct")
    class DeleteProductTests {
        @Test
        void deleteProduct() {
            // Arrange

            // Act

            // Assert
        }
    }

    @Nested
    @DisplayName("Tests for updateProduct")
    class UpdateProductTests {
        @Test
        void updateProduct() {
            // Arrange

            // Act

            // Assert
        }
    }

    @Nested
    @DisplayName("Tests for getProductById")
    class GetProductByIdProductTests {
        @Test
        void getProductById() {
            // Arrange

            // Act

            // Assert
        }
    }
}