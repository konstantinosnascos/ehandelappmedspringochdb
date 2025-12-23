package com.example.ecommerce.service;

import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.model.Inventory;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.InventoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class AdminProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public AdminProductService(ProductRepository productRepository,
                               InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Product createProduct(String sku, String name, String description,
                                 BigDecimal price, int initialStock) {

        Product product = new Product(sku, name, description, price);
        Product savedProduct = productRepository.save(product);

        Inventory inventory = new Inventory(savedProduct, initialStock);
        inventoryRepository.save(inventory);

        return savedProduct;
    }

    public void deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produkt hittades inte"));

        product.setActive(false);
        productRepository.save(product);
    }

    public void activateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produkt hittades inte"));

        product.setActive(true);
        productRepository.save(product);
    }

    public void updateStock(Long productId, int newStock) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Lagerpost saknas"));

        inventory.setInStock(newStock);
        inventoryRepository.save(inventory);
    }
}
