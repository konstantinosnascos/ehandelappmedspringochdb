package com.example.ecommerce.menu;

import com.example.ecommerce.model.*;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CheckoutMenu {

    private final OrderService orderService;
    private final CartService cartService;
    private final Scanner scanner = new Scanner(System.in);

    public CheckoutMenu(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    public void checkout(Customer customer) {

        Cart cart = cartService.getOrCreateCart(customer);

        if (cart.getItems().isEmpty()) {
            System.out.println("Varukorgen är tom.");
            return;
        }

        System.out.println("\n=== CHECKOUT ===");
        System.out.println("Vill du slutföra köpet?");
        System.out.println("1. Ja");
        System.out.println("0. Avbryt");
        System.out.print("Ditt val: ");

        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            Order order = orderService.createOrderFromCart(cart);
            cartService.clearCart(cart);

            System.out.println("Order skapad!");
            System.out.println("Order-ID: " + order.getId());
            System.out.println("Total: " + order.getTotal() + " kr");
        }
    }
}
