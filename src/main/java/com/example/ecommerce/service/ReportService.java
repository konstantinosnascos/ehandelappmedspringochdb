package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ReportService
{

    //Top-produkter, lågt lager, omsättning

    //Hämta top X mest sålda produkter
    //Hämta produkter med lågt lager
    //Hämta omsättning mellan datum

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ReportService(ProductRepository productRepository, OrderRepository orderRepository)
    {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<Object[]> getTopSellingProducts(int x)
    {
        return orderRepository.findTopSellingProducts(x);
    }

    public List<Product> getLowStockProducts(int threshold)
    {
        return productRepository.findProductsWithLowStock(threshold);
    }

    public BigDecimal getRevenueBetween(LocalDateTime start, LocalDateTime end)
    {
        BigDecimal revenue = orderRepository.calculateRevenueBetween(start, end);
        if(revenue != null)
        {
            return revenue;
        }
        else
        {
            return BigDecimal.ZERO;
        }
    }
}
