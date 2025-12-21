package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getOrCreateCart(Customer customer)
    {
        return cartRepository.findByCustomer(customer).orElseGet(() -> cartRepository.save(new Cart(customer)));
    }

    public void addProduct(Customer customer, Product product, int qty)
    {
        Cart cart = getOrCreateCart(customer);

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(ci -> ci.getProduct().equals(product))
                .findFirst();

        if(existing.isPresent())
        {
            existing.get().setQty(existing.get().getQty() + qty);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQty(qty);
            cart.getItems().add(item);


        }

        cartRepository.save(cart);

    }

    public void clearCart(Cart cart)
    {
        cartRepository.delete(cart);
    }
    @Transactional(readOnly = true)
    public Cart getCartWithItems(Customer customer)
    {
       Cart cart = cartRepository.findByCustomer(customer).orElseThrow(()-> new RuntimeException("Ingen varukorg hittades"));

       cart.getItems().size();
       return cart;
    }


}
