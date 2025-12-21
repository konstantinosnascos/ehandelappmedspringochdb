package com.example.ecommerce.menu;

import com.example.ecommerce.model.*;
import com.example.ecommerce.service.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class CartMenu {
    private final CartService cartService;
    private final Scanner scanner = new Scanner(System.in);

    public CartMenu(CartService cartService) {
        this.cartService = cartService;
    }

    public void show(Customer customer) {

        Optional<Cart> cartOptional = cartService.getCart(customer);

        if (cartOptional.isEmpty() || cartOptional.get().getItems().isEmpty()) {
            System.out.println("Din varukorg är tom");
            return;
        }

        Cart cart = cartOptional.get();

        System.out.println("\n=== DIN VARUKORG ===");

        BigDecimal total = BigDecimal.ZERO;
        int index = 1;

        for (CartItem item : cart.getItems()) {

            BigDecimal lineTotal =
                    item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQty()));

            total = total.add(lineTotal);

            System.out.printf(
                    "%d. %s | %d st | %s kr%n",
                    index++,
                    item.getProduct().getName(),
                    item.getQty(),
                    lineTotal
            );
        }


        System.out.println("-------------------");
        System.out.println("Total: " + total + " kr");

        System.out.println();
        System.out.println("1. Checkout");
        System.out.println("0. Tillbaka");
        System.out.print("Val: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("Checkout kommer här (nästa steg)");
                break;

            case "0":
                return;

            default:
                System.out.println("Ogiltigt val");
        }
    }
}
