package com.example.ecommerce.menu;

import com.example.ecommerce.model.Customer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class MainMenu {

    private Customer activeCustomer;

    private final CustomerMenu customerMenu;
    private final ProductMenu productMenu;
    private final CartMenu cartMenu;
    private final AdminProductMenu adminProductMenu;
    private final ReportMenu reportMenu;
    private final LoadScenarioMenu loadScenarioMenu;
    private final OrderMenu orderMenu;

    private final Scanner scanner = new Scanner(System.in);

    public MainMenu(CustomerMenu customerMenu,
                    ProductMenu productMenu,
                    CartMenu cartMenu,
                    AdminProductMenu adminProductMenu,
                    ReportMenu reportMenu, LoadScenarioMenu loadScenarioMenu, OrderMenu orderMenu) {

        this.customerMenu = customerMenu;
        this.productMenu = productMenu;
        this.cartMenu = cartMenu;
        this.adminProductMenu = adminProductMenu;
        this.reportMenu = reportMenu;
        this.loadScenarioMenu = loadScenarioMenu;
        this.orderMenu = orderMenu;
    }

    private void ensureCustomerExists() {
        if (activeCustomer == null) {
            activeCustomer = customerMenu.createCustomer();
        }
    }

    public void show() throws IOException {
        boolean running = true;

        while (running) {
            System.out.println("\n=== E-HANDEL ===");
            System.out.println("1. Visa produkter");
            System.out.println("2. Visa varukorg");
            System.out.println("3. Kundhantering");
            System.out.println("4. Produktadministration");
            System.out.println("5. Rapporter");
            System.out.println("6. Ladda Scenario");
            System.out.println("7. Orderhantering");
            System.out.println("0. Avsluta");
            System.out.print("Ditt val: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    productMenu.show(activeCustomer);
                    break;

                case "2":
                    ensureCustomerExists();
                    cartMenu.show(activeCustomer);
                    break;

                case "3":
                    customerMenu.show();
                    break;

                case "4":
                    adminProductMenu.show();
                    break;

                case "5":
                    reportMenu.show();
                    break;

                case "6":
                    loadScenarioMenu.show(scanner);
                    break;

                case "7":
                    orderMenu.showMenu();
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
