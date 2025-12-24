package com.example.ecommerce.menu;

import com.example.ecommerce.model.Customer;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class MainMenu {
    private Customer activeCustomer;
    private final CustomerMenu customerMenu;
    private final ProductMenu productMenu;
    private final CartMenu cartMenu;
    private final Scanner scanner = new Scanner(System.in);

    public MainMenu(CustomerMenu customerMenu, ProductMenu productMenu, CartMenu cartMenu) {
        this.customerMenu = customerMenu;
        this.productMenu = productMenu;
        this.cartMenu = cartMenu;
    }

    private void ensureCustomerExists() {
        if (activeCustomer == null) {
            activeCustomer = customerMenu.createCustomer();
        }
    }


    public void show() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== E-HANDEL ===");
            System.out.println("1. Visa produkter");
            System.out.println("2. Visa varukorg");
            System.out.println("3. Kundhantering");
            System.out.println("0. Avsluta");
            System.out.print("Ditt val (0-1): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    ensureCustomerExists();
                    productMenu.show(activeCustomer);
                    break;
                case "2":
                    ensureCustomerExists();
                    cartMenu.show(activeCustomer);
                    break;

                case "3":
                    customerMenu.show();
                    break;

                case "0":
                    System.out.println("Hej då!");
                    running = false;
                    break;

                default:
                    System.out.println("Ogiltigt val");
            }
        }
    }
}
