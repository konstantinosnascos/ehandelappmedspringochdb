package com.example.ecommerce.menu;

import com.example.ecommerce.helper.InputHelper;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMenu {
    private static final Logger logger = LoggerFactory.getLogger(CustomerMenu.class);

    private final InputHelper input;
    private final CustomerService customerService;

    public CustomerMenu(InputHelper input, CustomerService customerService) {
        this.input = input;
        this.customerService = customerService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            try {
                printMenu();
                int choice = input.getInt("Välj alternativ: ");
                switch (choice) {
                    case 1 -> createCustomer();
                    case 2 -> listCustomers();
                    case 3 -> updateCustomer();
                    case 4 -> deleteCustomer();
                    case 5 -> running = false;
                    case 6 -> sqlInjectionDemo();
                    default -> System.out.println("Ogiltigt val, försök igen!");
                }
            } catch (Exception e) {
                System.out.println("Ett fel uppstod: " + e.getMessage());
                logger.error("Fel i CustomerMenu", e);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== KUNDHANTERING ===");
        System.out.println("1. Registrera ny kund");
        System.out.println("2. Lista alla kunder");
        System.out.println("3. Uppdatera kund");
        System.out.println("4. Ta bort kund");
        System.out.println("5. Tillbaka till huvudmeny");
        System.out.println("6. Security demo (SQL injection)");
    }

    private void createCustomer() {
        String email = input.getString("Email: ");
        String name = input.getString("Namn: ");
        try {
            Customer c = customerService.createCustomer(email, name);
            System.out.println("Kund registrerad: " + c.getName() + " (" + c.getEmail() + ")");
        } catch (Exception e) {
            System.out.println("Fel vid registrering: " + e.getMessage());
        }
    }

    private void listCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("Inga kunder hittades.");
            return;
        }
        System.out.println("\n--- KUNDER ---");
        for (Customer c : customers) {
            System.out.printf("ID: %d | Namn: %s | Email: %s%n", c.getId(), c.getName(), c.getEmail());
        }
    }

    private void updateCustomer() {
        String email = input.getString("Ange email på kund att uppdatera: ");
        String newName = input.getString("Ange nytt namn: ");
        try {
            customerService.updateCustomer(email, newName);
            System.out.println("Kund uppdaterad.");
        } catch (Exception e) {
            System.out.println("Fel: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        String email = input.getString("Ange email på kund att ta bort: ");
        try {
            customerService.deleteCustomer(email);
            System.out.println("Kund borttagen.");
        } catch (Exception e) {
            System.out.println("Fel: " + e.getMessage());
        }
    }

    private void sqlInjectionDemo() {
        System.out.println("\n=== SQL INJECTION DEMO ===");
        System.out.println("Testa t.ex:");
        System.out.println("  normal email: anna@example.com");
        System.out.println("  injection:    ' OR '1'='1");
        System.out.println();

        String inputSQL = input.getString("Ange email: ");

        System.out.println("\n[OSÄKER SÖKNING]");
        customerService.findByEmailUnsafe(inputSQL)
                .ifPresentOrElse(
                        c -> System.out.println("HITTADE KUND: " + c.getEmail()),
                        () -> System.out.println("Ingen kund hittades")
                );

        System.out.println("\n[SÄKER SÖKNING]");
        customerService.findByEmail(inputSQL)
                .ifPresentOrElse(
                        c -> System.out.println("HITTADE KUND: " + c.getEmail()),
                        () -> System.out.println("Ingen kund hittades")
                );
    }
}