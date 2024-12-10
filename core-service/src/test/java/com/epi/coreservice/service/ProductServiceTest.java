package com.epi.coreservice.service;

import com.epi.coreservice.exc.NotFoundException;
import com.epi.coreservice.model.Category;
import com.epi.coreservice.model.Product;
import com.epi.coreservice.repository.ProductRepository;
import com.epi.coreservice.request.product.ProductUpdateRequest;
import com.epi.coreservice.client.FileStorageClient;
import com.epi.coreservice.request.product.ProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private FileStorageClient fileStorageClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    private ProductCreateRequest productCreateRequest;
    private ProductUpdateRequest productUpdateRequest;
    private MultipartFile file;
    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setName("Test Product");
        productCreateRequest.setDescription("Test Description");
        productCreateRequest.setCategoryId("1");

        productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setId("1");
        productUpdateRequest.setName("Updated Product");
        productUpdateRequest.setDescription("Updated Description");

        category = new Category();
        category.setName("Test Category");
        category.setId("1");
        product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .category(category)
                .keys(List.of("key1"))
                .build();
        product.setId("1");
    }

    @Test
    void createProduct_ShouldReturnProduct_WhenValidRequest() {

        when(categoryService.getCategoryById(anyString())).thenReturn(category);
        when(fileStorageClient.uploadImageToFIleSystem(file)).thenReturn(ResponseEntity.ok("imageId"));

        file = null;

        Product createdProduct = productService.createProduct(productCreateRequest, file);

        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldReturnProduct_WhenNoFileProvided() {

        when(categoryService.getCategoryById(anyString())).thenReturn(category);


        Product createdProduct = productService.createProduct(productCreateRequest, null);

        // Assert
        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
        assertNull(createdProduct.getImageId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenValidRequest() {
        // Arrange
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        when(fileStorageClient.uploadImageToFIleSystem(file)).thenReturn(ResponseEntity.ok("newImageId"));

        // Act
        Product updatedProduct = productService.updateProduct(productUpdateRequest, file);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("newImageId", updatedProduct.getImageId());
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void getProductById_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        // Arrange
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            productService.getProductById("nonExistingId");
        });
        assertEquals("Product not found", thrown.getMessage());
    }

    @Test
    void deleteProductById_ShouldCallDelete_WhenProductExists() {
        // Arrange
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));

        // Act
        productService.deleteProductById("1");

        // Assert
        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    void getProductsByCategoryId_ShouldReturnProducts_WhenCategoryExists() {
        // Arrange
        List<Product> productList = List.of(product);
        when(productRepository.getProductsByCategoryId(anyString())).thenReturn(productList);

        // Act
        List<Product> products = productService.getProductsByCategoryId("1");

        // Assert
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        verify(productRepository, times(1)).getProductsByCategoryId("1");
    }

}