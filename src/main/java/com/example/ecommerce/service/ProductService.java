package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listActiveProductsForConsole()
    {
        return productRepository.findByActive(true);
    }

    public Page<Product> listActiveProducts(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable);
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findActiveProductsByCategoryName(categoryName);
    }

    public Optional<Product> getProductBySku(String sku)
    {
        return productRepository.findBySku(sku);

    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public Product createProduct(Product product)
    {
        if(productRepository.existsBySku(product.getSku()))
        {
            throw new IllegalArgumentException("Product with SKU" + product.getSku() + " already exists");
        } return productRepository.save(product);
    }
    public List<Product> listInactiveProducts() {
        return productRepository.findByActive(false);
    }

    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }
}
