package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

//Skapad för att generera Testdata
@Service
public class DataGeneratorService
{
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public DataGeneratorService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void generateOrders(int amount) {

        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (customers.isEmpty() || products.isEmpty()) {
            throw new IllegalStateException("Kunder eller produkter saknas för att skapa köphistorik");
        }

        Random random = new Random();

        for (int i = 0; i < amount; i++) {

            Customer customer = customers.get(random.nextInt(customers.size()));

            Order order = new Order();
            order.setCustomer(customer);
            order.setStatus(OrderStatus.PAID);

            BigDecimal total = BigDecimal.ZERO;

            int antalProdukter = 1 + random.nextInt(5);

            for (int j = 0; j < antalProdukter; j++) {

                Product product = products.get(random.nextInt(products.size()));
                int qty = 1 + random.nextInt(3);

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQty(qty);
                item.setUnitPrice(product.getPrice());

                BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(qty));
                item.setLineTotal(lineTotal);

                order.getItems().add(item);
                total = total.add(lineTotal);
            }

            order.setTotal(total);
            orderRepository.save(order);
        }
    }
}
