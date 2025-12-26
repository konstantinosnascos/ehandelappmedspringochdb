package com.example.ecommerce.service;

import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTests {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private CartService cartService;

    private Customer customer;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        customer = new Customer("test@email.se", "Test User");
        product = new Product("TEST-001", "Test Product", "Description", BigDecimal.valueOf(100.00));

        // Sätt ID på product
        ReflectionTestUtils.setField(product, "id", 1L);

        cart = new Cart(customer);
    }

    @Test
    void shouldCreateNewCartWhenNotExists() {
        when(cartRepository.findByCustomerWithItems(customer)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        Cart result = cartService.getOrCreateCart(customer);

        assertThat(result).isNotNull();
        assertThat(result.getCustomer()).isEqualTo(customer);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void shouldReturnExistingCart() {
        when(cartRepository.findByCustomerWithItems(customer)).thenReturn(Optional.of(cart));

        Cart result = cartService.getOrCreateCart(customer);

        assertThat(result).isEqualTo(cart);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void shouldAddProductToCartWhenStockAvailable() {
        when(cartRepository.findByCustomerWithItems(customer)).thenReturn(Optional.of(cart));
        when(inventoryService.hasStock(1L, 2)).thenReturn(true);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        cartService.addProduct(customer, product, 2);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQty()).isEqualTo(2);
        verify(cartRepository).save(cart);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock() {
        when(cartRepository.findByCustomerWithItems(customer)).thenReturn(Optional.of(cart));
        when(inventoryService.hasStock(1L, 100)).thenReturn(false);

        assertThatThrownBy(() -> cartService.addProduct(customer, product, 100))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Otillräckligt lager");

        verify(cartRepository).findByCustomerWithItems(customer);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void shouldIncreaseQuantityWhenProductAlreadyInCart() {

        CartItem existingItem = new CartItem();
        existingItem.setCart(cart);
        existingItem.setProduct(product);
        existingItem.setQty(2);
        cart.getItems().add(existingItem);

        when(cartRepository.findByCustomerWithItems(customer)).thenReturn(Optional.of(cart));

        when(inventoryService.hasStock(1L, 5)).thenReturn(true);

        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        cartService.addProduct(customer, product, 3);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQty()).isEqualTo(5);

        verify(inventoryService).hasStock(1L, 5);
    }

    @Test
    void shouldClearCart() {
        cartService.clearCart(cart);

        verify(cartRepository).delete(cart);
    }

    @Test
    void shouldGetCartWithItems() {
        when(cartRepository.findByCustomerWithItems(customer)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartWithItems(customer);

        assertThat(result).isEqualTo(cart);
        verify(cartRepository).findByCustomerWithItems(customer);
    }

    @Test
    void shouldThrowExceptionWhenCartNotFound() {
        when(cartRepository.findByCustomerWithItems(customer)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCartWithItems(customer))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ingen varukorg hittades");
    }
}