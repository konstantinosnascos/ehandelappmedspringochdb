package com.example.ecommerce.menu;

import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class MainMenu {

    private final CustomerMenu customerMenu;
    private final ProductMenu productMenu;
    private final Scanner scanner = new Scanner(System.in);

    public MainMenu(CustomerMenu customerMenu, ProductMenu productMenu) {
        this.customerMenu = customerMenu;
        this.productMenu = productMenu;
    }

    public void show() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== E-HANDEL ===");
            System.out.println("1. Visa produkter");
            System.out.println("0. Avsluta");
            System.out.print("Ditt val (0-1): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    productMenu.show();
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
