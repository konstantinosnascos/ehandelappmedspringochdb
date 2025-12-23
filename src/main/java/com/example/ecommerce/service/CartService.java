package com.example.ecommerce.service;

import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final InventoryService inventoryService;


    public CartService(CartRepository cartRepository, InventoryService inventoryService) {
        this.cartRepository = cartRepository;
        this.inventoryService = inventoryService;
    }
    @Transactional
    public Cart getOrCreateCart(Customer customer)
    {
        return cartRepository.findByCustomerWithItems(customer).orElseGet(() -> cartRepository.save(new Cart(customer)));
    }

    @Transactional(readOnly = true)
    public Cart getCart(Customer customer)
    {
        return cartRepository.findByCustomerWithItems(customer).orElse(null);
    }

    public void addProduct(Customer customer, Product product, int qty)
    {
        if (!inventoryService.hasStock(product.getId(), qty)) {
            throw new InsufficientStockException(
                    "Otillräckligt lager för " + product.getName()
            );
        }
        Cart cart = getOrCreateCart(customer);

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(ci -> ci.getProduct().equals(product))
                .findFirst();

        if(existing.isPresent())
        {
            int newQty = existing.get().getQty() + qty;
            if (!inventoryService.hasStock(product.getId(), newQty)) {
                throw new InsufficientStockException("Otillräckligt lager");
            }
            existing.get().setQty(newQty);
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
       Cart cart = cartRepository.findByCustomerWithItems(customer).orElseThrow(()-> new RuntimeException("Ingen varukorg hittades"));

       cart.getItems().size();
       return cart;
    }


}
