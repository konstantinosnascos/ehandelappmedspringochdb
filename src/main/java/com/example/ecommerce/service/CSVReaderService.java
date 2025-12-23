package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.CustomerRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

public class CSVReaderService
{
    private final CustomerRepository customerRepository;
    private final CategoryRepository categoryRepository;

    public CSVReaderService(CustomerRepository customerRepository,
                            CategoryRepository categoryRepository)
    {
        this.customerRepository = customerRepository;
        this.categoryRepository = categoryRepository;
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
                    .forEach(customerRepository :: save)
            ;
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
                    .forEach
                            (cols ->
                                    {
                                        String sku = cols[0].trim();
                                        String name = cols[1].trim();
                                        String description = cols[2].trim();
                                        BigDecimal price = new BigDecimal(cols[3].trim());
                                        String[] categoryNames = cols[4].split(";");

                                        Product product = new Product(sku, name, description, price);
                                        for (String categoryName : categoryNames)
                                        {
                                            Category category = categoryRepository
                                                    .findByNameIgnoreCase(categoryName.trim())
                                                    .orElseGet
                                                            (() ->
                                                                    categoryRepository.save(new Category(categoryName.trim()))
                                                            )
                                                    ;
                                        }
                                    }
                            )
            ;
        }
    }
}