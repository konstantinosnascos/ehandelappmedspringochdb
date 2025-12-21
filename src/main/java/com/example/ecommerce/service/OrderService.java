package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrderFromCart(Cart cart)
    {
        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setStatus(OrderStatus.NEW);

        BigDecimal total = BigDecimal.ZERO;

        for(CartItem ci : cart.getItems())
        {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(ci.getProduct());
            item.setQty(ci.getQty());
            item.setUnitPrice(ci.getProduct().getPrice());
            BigDecimal lineTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQty()));

            item.setLineTotal(lineTotal);
            total = total.add(lineTotal);

            order.getItems().add(item);
        }
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);
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
