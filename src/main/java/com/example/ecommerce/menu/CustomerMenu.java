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

    public Customer createCustomer() {
        while(true){
            try {

                System.out.println("\n=== KUNDUPPGIFTER FÖR ATT LÄGGA TILL I VARUKORGEN ===");

                System.out.print("Email: ");
                String email = scanner.nextLine();

                System.out.print("Namn: ");
                String name = scanner.nextLine();

                Customer customer = customerService.createCustomer(email, name);

                System.out.println("Kund registrerad!");
                return customer;
            } catch (IllegalArgumentException e) {
                System.out.println("Fel: " + e.getMessage());
                System.out.println("Försök igen");
            }
        }

    }
}

