package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Product inactiveProduct;

    @BeforeEach
    void setUp() {
        product1 = new Product("PROD-001", "Active Product 1", "Description", BigDecimal.valueOf(100.00));
        product1.setActive(true);

        product2 = new Product("PROD-002", "Active Product 2", "Description", BigDecimal.valueOf(200.00));
        product2.setActive(true);

        inactiveProduct = new Product("PROD-003", "Inactive Product", "Description", BigDecimal.valueOf(150.00));
        inactiveProduct.setActive(false);
    }

    @Test
    void shouldListActiveProducts() {
        when(productRepository.findByActive(true)).thenReturn(Arrays.asList(product1, product2));

        List<Product> result = productService.listActiveProducts();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(product1, product2);
        assertThat(result).allMatch(Product::isActive);
    }

    @Test
    void shouldNotIncludeInactiveProducts() {
        when(productRepository.findByActive(true)).thenReturn(Arrays.asList(product1, product2));

        List<Product> result = productService.listActiveProducts();

        assertThat(result).doesNotContain(inactiveProduct);
    }

    @Test
    void shouldGetProductsByCategory() {
        when(productRepository.findByNameContainingIgnoreCase("Elektronik"))
                .thenReturn(Arrays.asList(product1));

        List<Product> result = productService.getProductsByCategory("Elektronik");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(product1);
    }

    @Test
    void shouldGetProductBySku() {
        when(productRepository.findBySku("PROD-001")).thenReturn(Optional.of(product1));

        Optional<Product> result = productService.getProductBySku("PROD-001");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(product1);
    }

    @Test
    void shouldReturnEmptyWhenSkuNotFound() {
        when(productRepository.findBySku("NONEXISTENT")).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductBySku("NONEXISTENT");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldCreateProduct() {
        Product newProduct = new Product("NEW-001", "New Product", "Desc", BigDecimal.valueOf(50.00));

        when(productRepository.existsBySku("NEW-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        Product result = productService.createProduct(newProduct);

        assertThat(result).isNotNull();
        assertThat(result.getSku()).isEqualTo("NEW-001");
        verify(productRepository).save(newProduct);
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateSku() {
        Product duplicateProduct = new Product("PROD-001", "Duplicate", "Desc", BigDecimal.valueOf(100.00));

        when(productRepository.existsBySku("PROD-001")).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(duplicateProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product with SKUPROD-001 already exists");

        verify(productRepository, never()).save(any(Product.class));
    }
}