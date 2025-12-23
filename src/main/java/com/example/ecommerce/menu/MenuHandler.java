package com.example.ecommerce.menu;

import com.example.ecommerce.helper.InputHelper;
import org.springframework.stereotype.Component;

@Component
public class MenuHandler {
    private final InputHelper input;
    private final ProductMenu productMenu;
    private final CustomerMenu customerMenu;
    private final CartMenu cartMenu;
    private final OrderMenu orderMenu;
    private final ReportMenu reportMenu;
    private final SystemMenu systemMenu;

    public MenuHandler(InputHelper input,
                       ProductMenu productMenu,
                       CustomerMenu customerMenu,
                       CartMenu cartMenu,
                       OrderMenu orderMenu,
                       ReportMenu reportMenu,
                       SystemMenu systemMenu) {
        this.input = input;
        this.productMenu = productMenu;
        this.customerMenu = customerMenu;
        this.cartMenu = cartMenu;
        this.orderMenu = orderMenu;
        this.reportMenu = reportMenu;
        this.systemMenu = systemMenu;
    }

    public void runMainMenu() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = input.getInt("Välj alternativ: ");
            switch (choice) {
                case 1 -> productMenu.run();
                case 2 -> customerMenu.run();
                case 3 -> cartMenu.run();
                case 4 -> orderMenu.run();
                case 5 -> reportMenu.run();
                case 6 -> systemMenu.run();
                case 7 -> running = false;
                default -> System.out.println("Ogiltigt val.");
            }
        }
        System.out.println("Avslutar...");
    }

    private void printMainMenu() {
        System.out.println("\n=== HUVUDMENY ===");
        System.out.println("1. Produkter");
        System.out.println("2. Kunder");
        System.out.println("3. Kundvagn");
        System.out.println("4. Order & Checkout");
        System.out.println("5. Rapporter");
        System.out.println("6. System & Import");
        System.out.println("7. Avsluta");
    }
}
