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

    public void createCustomer()
    {
        System.out.println("\n=== KUNDUPPGIFTER ===");

        System.out.println("Email: ");
        String email = scanner.nextLine();

        System.out.println("Namn: ");
        String name = scanner.nextLine();

        customerService.createCustomer(email, name);

        System.out.println("Kund registrerad!");
    }
}
