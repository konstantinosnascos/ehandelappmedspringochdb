package com.example.ecommerce.dto;

public record LowStockDTO(
        String productName,
        int inStock
) {}