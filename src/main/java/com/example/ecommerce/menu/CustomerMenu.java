package com.example.ecommerce.menu;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.service.CustomerService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomerMenu {

    private final CustomerService customerService;
    private final Scanner scanner = new Scanner(System.in);

    public CustomerMenu(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void show() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== KUNDHANTERING ===");
            System.out.println("1. Skapa ny kund");
            System.out.println("2. Lista alla kunder");
            System.out.println("3. Sök kund via email");
            System.out.println("0. Tillbaka");
            System.out.print("Val: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createCustomer();
                    break;
                case "2":
                    listCustomers();
                    break;
                case "3":
                    searchCustomer();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Ogiltigt val");
            }
        }
    }


    public Customer createCustomer() {
        while(true){
            try {

        while (true) {
            System.out.println("\n=== KUNDUPPGIFTER ===");

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Namn: ");
            String name = scanner.nextLine().trim();

            // 1. Båda tomma
            if (email.isEmpty() && name.isEmpty()) {
                System.out.println("Du måste fylla i din email adress och ditt namn, försök igen!");
                continue;
            }

            // 2. Email tom
            if (email.isEmpty()) {
                System.out.println("Du måste fylla i din email adress, försök igen!");
                continue;
            }

            // 3. Namn tomt
            if (name.isEmpty()) {
                System.out.println("Du måste fylla i ditt namn, försök igen!");
                continue;
            }

            // 4. Felaktigt email-format
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|se|net)$")) {
                System.out.println("Din email adress är inte skriven i rätt format.");
                continue;
            }

            // 5. Allt korrekt → försök skapa kund
            try {
                Customer customer = customerService.createCustomer(email, name);
                System.out.println("Kund registrerad!");
                return customer;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Försök igen.");
            }
        }
    }


    private void listCustomers() {
        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("Inga kunder finns.");
            return;
        }

        System.out.println("\n--- KUNDER ---");
        for (Customer c : customers) {
            System.out.println(
                    c.getId() + " | " + c.getName() + " | " + c.getEmail()
            );
        }
    }

    private void searchCustomer() {
        System.out.print("Ange email: ");
        String email = scanner.nextLine();

        Optional<Customer> customer = customerService.findByEmail(email);

        if (customer.isPresent()) {
            Customer c = customer.get();
            System.out.println(
                    c.getId() + " | " + c.getName() + " | " + c.getEmail()
            );
        } else {
            System.out.println("Kund hittades inte");
        }
    }

    


}

