package com.example.ecommerce.service;

import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.model.Inventory;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.InventoryRepository;
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
class InventoryServiceTests {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Product product;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        product = new Product("TEST-001", "Test Product", "Description", BigDecimal.valueOf(100.00));
        inventory = new Inventory(product, 50);
    }

    @Test
    void shouldGetStock() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));

        Optional<Inventory> result = inventoryService.getStock(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getInStock()).isEqualTo(50);
    }

    @Test
    void shouldGetStockLevel() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));

        int result = inventoryService.getStockLevel(1L);

        assertThat(result).isEqualTo(50);
    }

    @Test
    void shouldReturnZeroWhenProductNotFound() {
        when(inventoryRepository.findById(999L)).thenReturn(Optional.empty());

        int result = inventoryService.getStockLevel(999L);

        assertThat(result).isEqualTo(0);
    }

    @Test
    void shouldCheckIfHasStock() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));

        boolean result = inventoryService.hasStock(1L, 30);

        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenInsufficientStock() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));

        boolean result = inventoryService.hasStock(1L, 100);

        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseWhenProductNotFoundForStockCheck() {
        when(inventoryRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = inventoryService.hasStock(999L, 10);

        assertThat(result).isFalse();
    }

    @Test
    void shouldGetLowStock() {
        Product p1 = new Product("LOW-001", "Low Stock 1", "Desc", BigDecimal.valueOf(100));
        Product p2 = new Product("LOW-002", "Low Stock 2", "Desc", BigDecimal.valueOf(100));

        Inventory inv1 = new Inventory(p1, 3);
        Inventory inv2 = new Inventory(p2, 2);

        when(inventoryRepository.findByInStockLessThan(5)).thenReturn(Arrays.asList(inv1, inv2));

        List<Inventory> result = inventoryService.getLowStock(5);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getInStock()).isLessThan(5);
        assertThat(result.get(1).getInStock()).isLessThan(5);
    }

    @Test
    void shouldDecreaseStock() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArguments()[0]);

        inventoryService.decrease(1L, 10);

        assertThat(inventory.getInStock()).isEqualTo(40);
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void shouldThrowExceptionWhenDecreasingBeyondAvailable() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));

        assertThatThrownBy(() -> inventoryService.decrease(1L, 100))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Otillräckligt lager för produkt 1");

        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void shouldThrowExceptionWhenDecreasingNonExistentProduct() {
        when(inventoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryService.decrease(999L, 10))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Produkt 999 finns inte");
    }

    @Test
    void shouldIncreaseStock() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArguments()[0]);

        inventoryService.increase(1L, 20);

        assertThat(inventory.getInStock()).isEqualTo(70);
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void shouldThrowExceptionWhenIncreasingNonExistentProduct() {
        when(inventoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryService.increase(999L, 10))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Produkt 999 finns inte");
    }

    @Test
    void shouldDecreaseStockToZero() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArguments()[0]);

        inventoryService.decrease(1L, 50);

        assertThat(inventory.getInStock()).isEqualTo(0);
    }
}