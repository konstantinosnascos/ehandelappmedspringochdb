package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Customer customer, Product product, int quantity)
    {
        Order order = new Order();
        order.setCustomer(customer);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQty(quantity);
        item.setUnitPrice(product.getPrice());

        order.getItems().add(item);

        return orderRepository.save(order);
    }

    //Hämta alla ordrar
    //    Hämta order med id
    //    Hämta ordrar för kund
    //    Hämta ordrar med viss status
    //    Skapa order
    //    Avbryt order
    //    Sätt status PAID

}
