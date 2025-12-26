package com.example.ecommerce.menu;

import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.service.AdminProductService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AdminProductMenu {

    private final AdminProductService adminProductService;
    private final Scanner scanner = new Scanner(System.in);

    public AdminProductMenu(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    public void show() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== PRODUKTADMIN ===");
            System.out.println("1. Lägg till produkt");
            System.out.println("2. Ändra produkt");
            System.out.println("3. Inaktivera produkt");
            System.out.println("4. Aktivera produkt");
            System.out.println("5. Uppdatera lager");
            System.out.println("0. Tillbaka");
            System.out.print("Val: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addProduct();
                case "2" -> updateProduct();
                case "3" -> deactivateProduct();
                case "4" -> activateProduct();
                case "5" -> updateStock();
                case "0" -> running = false;
                default -> System.out.println("Ogiltigt val");
            }
        }
    }

    private void addProduct() {
        try
        {
            System.out.println("\n=== LÄGG TILL PRODUKT ===");

            System.out.print("SKU: ");
            String sku = scanner.nextLine();

            System.out.print("Namn: ");
            String name = scanner.nextLine();

            System.out.print("Beskrivning: ");
            String description = scanner.nextLine();

            System.out.print("Pris: ");
            BigDecimal price = new BigDecimal(scanner.nextLine());

            System.out.print("Startlager: ");
            int stock = Integer.parseInt(scanner.nextLine());

            adminProductService.addProduct(sku, name, description, price, stock);

            System.out.println("✔ Produkten skapades!");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Error:" + e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Error" + e.getMessage());
        }
    }

    private void updateProduct() {
        System.out.println("\n=== ÄNDRA PRODUKT ===");

        System.out.print("Produkt-ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Nytt namn (lämna tomt för att behålla): ");
        String name = scanner.nextLine();
        if (name.isBlank()) name = null;

        System.out.print("Ny beskrivning (lämna tomt för att behålla): ");
        String description = scanner.nextLine();
        if (description.isBlank()) description = null;

        System.out.print("Nytt pris (lämna tomt för att behålla): ");
        String priceInput = scanner.nextLine();
        BigDecimal price = priceInput.isBlank() ? null : new BigDecimal(priceInput);

        adminProductService.updateProduct(id, name, description, price);

        System.out.println("✔ Produkten uppdaterades!");
    }

    private void deactivateProduct() {
        try {
            System.out.println("\n=== INAKTIVERA PRODUKT ===");

            System.out.print("Produkt-ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            adminProductService.deactivateProduct(id);

            System.out.println("✔ Produkten är nu inaktiverad.");
        }
        catch (ProductNotFoundException e)
        {
            System.out.println("Product not found: " + e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void activateProduct() {
        try
        {
            System.out.println("\n=== AKTIVERA PRODUKT ===");

            System.out.print("Produkt-ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            adminProductService.activateProduct(id);

            System.out.println("✔ Produkten är nu aktiv igen.");
        }
        catch (ProductNotFoundException e)
        {
            System.out.println("Product not found: " + e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateStock() {
        System.out.println("\n=== UPPDATERA LAGER ===");

        System.out.print("Produkt-ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Nytt lagersaldo: ");
        int stock = Integer.parseInt(scanner.nextLine());

        adminProductService.updateStock(id, stock);

        System.out.println("✔ Lagret uppdaterades!");
    }
}
