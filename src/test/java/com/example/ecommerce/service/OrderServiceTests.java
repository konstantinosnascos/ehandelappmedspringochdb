package com.example.ecommerce.service;

import com.example.ecommerce.exception.OrderNotFoundException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product product1;
    private Product product2;
    private Cart cart;

    @BeforeEach
    void setUp() {
        customer = new Customer("test@email.se", "Test User");
        product1 = new Product("PROD-001", "Product 1", "Desc", BigDecimal.valueOf(100.00));
        product2 = new Product("PROD-002", "Product 2", "Desc", BigDecimal.valueOf(50.00));

        cart = new Cart(customer);

        CartItem item1 = new CartItem();
        item1.setCart(cart);
        item1.setProduct(product1);
        item1.setQty(2);

        CartItem item2 = new CartItem();
        item2.setCart(cart);
        item2.setProduct(product2);
        item2.setQty(3);

        cart.getItems().add(item1);
        cart.getItems().add(item2);
    }

    @Test
    void shouldCreateOrderFromCart() {
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order order = (Order) i.getArguments()[0];
            return order;
        });

        Order result = orderService.createOrderFromCart(cart);

        assertThat(result).isNotNull();
        assertThat(result.getCustomer()).isEqualTo(customer);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.NEW);
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotal()).isEqualByComparingTo(new BigDecimal("350.00"));

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldCalculateCorrectOrderTotal() {
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.createOrderFromCart(cart);

        BigDecimal expectedTotal = BigDecimal.valueOf(100.00)
                .multiply(BigDecimal.valueOf(2))
                .add(BigDecimal.valueOf(50.00).multiply(BigDecimal.valueOf(3)));

        assertThat(result.getTotal()).isEqualByComparingTo(expectedTotal);
    }

    @Test
    void shouldSetCorrectOrderItemDetails() {
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.createOrderFromCart(cart);

        OrderItem firstItem = result.getItems().get(0);
        assertThat(firstItem.getProduct()).isEqualTo(product1);
        assertThat(firstItem.getQty()).isEqualTo(2);
        assertThat(firstItem.getUnitPrice()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(firstItem.getLineTotal()).isEqualByComparingTo(BigDecimal.valueOf(200.00));
    }

    @Test
    void shouldMarkOrderAsPaid() {
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        orderService.markAsPaid(1L);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowExceptionWhenMarkingNonExistentOrderAsPaid() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.markAsPaid(999L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order 999 finns inte");
    }

    @Test
    void shouldCancelOrder() {
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        orderService.cancelOrder(1L);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowExceptionWhenCancellingNonExistentOrder() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.cancelOrder(999L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order 999 finns inte");
    }

    @Test
    void shouldCreateEmptyOrderFromEmptyCart() {
        Cart emptyCart = new Cart(customer);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.createOrderFromCart(emptyCart);

        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}