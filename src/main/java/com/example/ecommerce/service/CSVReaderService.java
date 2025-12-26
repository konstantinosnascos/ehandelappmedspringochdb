package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class CSVReaderService
{
    private final CustomerRepository customerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CSVReaderService(CustomerRepository customerRepository,
                            CategoryRepository categoryRepository, ProductRepository productRepository)
    {
        this.customerRepository = customerRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public void customers (Path csvFile) throws IOException
    {
        try (BufferedReader reader = Files.newBufferedReader(csvFile))
        {
            reader.lines()
                    .skip(1)
                    .map(line -> line.split(","))
                    .filter(cols ->cols.length >= 2)
                    .map(cols -> new Customer(cols[0].trim(), cols[1].trim()))
                    .forEach(customerRepository::save);
        }
    }

    public void categories (Path csvFile) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(csvFile))
        {
            reader.lines()
                    .skip(1)
                    .map(String::trim)
                    .filter(name -> !name.isEmpty())
                    .forEach
                            (name ->
                                    {
                                        categoryRepository
                                                .findByNameIgnoreCase(name)
                                                .orElseGet(() -> categoryRepository.save(new Category(name)))
                                        ;
                                    }
                            )
            ;
        }
    }

    public void products (Path csvFile) throws IOException
    {
        try (BufferedReader reader = Files.newBufferedReader(csvFile))
        {
            reader.lines()
                .skip(1)
                .map(line -> line.split(","))
                .filter(cols -> cols.length >= 5)
                .forEach(cols ->
                {
                    String sku = cols[0].trim();
                    if (productRepository.existsBySku(sku))
                        return;

                    Product product = new Product(sku, cols[1].trim(), cols[2].trim(),
                            new BigDecimal(cols[3].trim()));

                    String[] categoryNames = cols[4].trim().split(";");
                    for (String categoryName : categoryNames)
                    {
                        Category category = categoryRepository.findByNameIgnoreCase(categoryName.trim()).orElseGet(() ->
                                categoryRepository.save(new Category(categoryName.trim())));

                        product.getCategories().add(category);
                    }

                    productRepository.save(product);
                });
        }
    }
}