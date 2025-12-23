package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listActiveProducts()
    {
        return productRepository.findByActive(true);
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository
                .findByCategoriesNameIgnoreCase(categoryName)
                .stream()
                .filter(Product::isActive)
                .toList();
    }

    public Optional<Product> getProductBySku(String sku)
    {
        return productRepository.findBySku(sku);

    }

    public Product createProduct(Product product)
    {
        if(productRepository.existsBySku(product.getSku()))
        {
            throw new IllegalArgumentException("Product with SKU" + product.getSku() + " already exists");
        } return productRepository.save(product);
    }

}
