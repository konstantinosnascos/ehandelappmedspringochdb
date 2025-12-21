package com.example.ecommerce.repository;

import com.example.ecommerce.model.Inventory;
import com.example.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
class InventoryRepositoryTests {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;
    private Product product3;

    @Test
    void shouldSaveAndFindInventory() {

        Product product = productRepository.save(
                new Product("TEST-INV-001", "Testprodukt", "Desc", new BigDecimal("100.00"))
        );

        Inventory inventory = new Inventory(product, 50);
        Inventory saved = inventoryRepository.save(inventory);

        Optional<Inventory> found = inventoryRepository.findById(saved.getProductId());

        assertThat(found).isPresent();
        assertThat(found.get().getInStock()).isEqualTo(50);
    }

    @Test
    void shouldFindLowStockProducts() {
        Product p1 = productRepository.save(new Product("LOW-001", "Produkt 1", "Desc", new BigDecimal("100.00")));
        Product p2 = productRepository.save(new Product("LOW-002", "Produkt 2", "Desc", new BigDecimal("200.00")));
        Product p3 = productRepository.save(new Product("LOW-003", "Produkt 3", "Desc", new BigDecimal("300.00")));

        inventoryRepository.save(new Inventory(p1, 3));
        inventoryRepository.save(new Inventory(p2, 50));
        inventoryRepository.save(new Inventory(p3, 2));

        List<Inventory> lowStock = inventoryRepository.findByInStockLessThan(5);

        assertThat(lowStock).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldUpdateStockLevel() {
        Product product = productRepository.save(
                new Product("UPD-INV-001", "Updateprodukt", "Desc", new BigDecimal("100.00"))
        );
        Inventory inventory = inventoryRepository.save(new Inventory(product, 100));

        inventory.setInStock(75);
        inventoryRepository.save(inventory);

        Inventory updated = inventoryRepository.findById(product.getId()).orElseThrow();
        assertThat(updated.getInStock()).isEqualTo(75);
    }

    @Test
    void shouldReturnEmptyWhenInventoryNotFound() {
        Optional<Inventory> result = inventoryRepository.findById(99999L);
        assertThat(result).isEmpty();
    }
}