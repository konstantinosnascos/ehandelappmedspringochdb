package com.example.ecommerce.menu;

import com.example.ecommerce.model.*;
import com.example.ecommerce.service.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class CartMenu {
    private final CartService cartService;
    private final CheckoutMenu checkoutMenu;
    private final Scanner scanner = new Scanner(System.in);


    public CartMenu(CartService cartService, CheckoutMenu checkoutMenu) {
        this.cartService = cartService;
        this.checkoutMenu = checkoutMenu;
    }

    public void show(Customer customer) {

        Cart cart;
        try{
            cart = cartService.getCartWithItems(customer);
        } catch (Exception e) {
            System.out.println("Din varukorg är tom");
            return;
        }

        if(cart.getItems().isEmpty())
        {
            System.out.println("Din varukorg är tom");
            return;
        }

        System.out.println("\n=== DIN VARUKORG ===");

        BigDecimal total = BigDecimal.ZERO;
        int index = 1;

        for (CartItem item : cart.getItems()) {

            BigDecimal lineTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQty()));
            total = total.add(lineTotal);

            System.out.println(index++ + ". " + item.getProduct().getName());
            System.out.println("   Antal: " + item.getQty());
            System.out.println("   Pris/st: " + item.getProduct().getPrice() + " kr");
            System.out.println("   Radtotal: " + lineTotal + " kr");
            System.out.println();
        }


        System.out.println("-------------------");
        System.out.println("Total: " + total + " kr");

        System.out.println();
        System.out.println("1. Checkout");
        System.out.println("2. Ta bort produkter");
        System.out.println("0. Tillbaka");
        System.out.print("Val: ");

        String choice = scanner.nextLine();

        switch(choice) {
            case "1":
                checkoutMenu.checkout(cart);
                break;
            case "2":
                removeFromCart(cart);
                break;
            case "0":
                return;
            default:
                System.out.println("Ogiltigt val");
        }

    }
    private void removeFromCart(Cart cart) {
        System.out.print("Välj produktnummer att ta bort: ");

        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            CartItem item = cart.getItems().get(index);

            cartService.removeProduct(cart.getCustomer(), item.getProduct());

            System.out.println("Produkten togs bort ur varukorgen");

        } catch (Exception e) {
            System.out.println("Ogiltigt val");
        }
    }

}
