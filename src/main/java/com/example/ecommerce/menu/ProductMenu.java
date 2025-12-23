package com.example.ecommerce.menu;

import com.example.ecommerce.helper.InputHelper;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.InventoryService;
import com.example.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ProductMenu {
    private static final Logger logger = LoggerFactory.getLogger(ProductMenu.class);
    private final InputHelper input;
    private final ProductService productService;
    private final InventoryService inventoryService;

    public ProductMenu(InputHelper input, ProductService productService, InventoryService inventoryService) {
        this.input = input;
        this.productService = productService;
        this.inventoryService = inventoryService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            try {
                printMenu();
                int choice = input.getInt("Välj alternativ: ");
                switch (choice) {
                    case 1 -> listProducts();
                    case 2 -> searchProduct();
                    case 3 -> addProduct();
                    case 4 -> disableProduct();
                    case 5 -> activateProduct();
                    case 6 -> running = false;
                    default -> System.out.println("Ogiltigt val, försök igen!");
                }
            } catch (Exception e) {
                System.out.println("Ett fel uppstod: " + e.getMessage());
                logger.error("Fel i ProductMenu", e);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== PRODUKTHANTERING ===");
        System.out.println("1. Lista alla aktiva produkter");
        System.out.println("2. Sök produkt (SKU)");
        System.out.println("3. Lägg till ny produkt");
        System.out.println("4. Inaktivera produkt");
        System.out.println("5. Aktivera produkt");
        System.out.println("6. Tillbaka till huvudmeny");
    }

    private void listProducts() {
        List<Product> products = productService.listActiveProducts();
        if (products.isEmpty()) {
            System.out.println("Inga aktiva produkter hittades.");
            return;
        }
        System.out.println("\n--- PRODUKTER ---");
        for (Product p : products) {
            System.out.printf("SKU: %s | Namn: %s | Pris: %s kr%n", p.getSku(), p.getName(), p.getPrice());
        }
    }

    private void searchProduct() {
        String sku = input.getString("Ange SKU: ");
        Optional<Product> product = productService.getProductBySku(sku);
        if (product.isPresent()) {
            System.out.println("Hittad: " + product.get());
        } else {
            System.out.println("Ingen produkt med den SKU hittades.");
        }
    }

    private void addProduct() {
        String sku = input.getString("SKU: ");
        String name = input.getString("Namn: ");
        String desc = input.getString("Beskrivning: ");
        double price = input.getDouble("Pris: ");
        int stock = input.getInt("Initialt lagerantal: ");

        try {
            Product newProduct = new Product(sku, name, desc, BigDecimal.valueOf(price));
            Product saved = productService.createProduct(newProduct);
            inventoryService.createInventory(saved, stock);
            System.out.println("Produkt skapad!");
        } catch (Exception e) {
            System.out.println("Kunde inte skapa produkt: " + e.getMessage());
        }
    }

    private void disableProduct() {
        String sku = input.getString("Ange SKU att inaktivera: ");
        Optional<Product> product = productService.getProductBySku(sku);
        if (product.isPresent()) {
            Product p = product.get();
            p.setActive(false);
            productService.updateProduct(p);
            System.out.println("Produkt inaktiverad.");
        } else {
            System.out.println("Produkt hittades inte.");
        }
    }
    private void activateProduct() {
        List<Product> inactive = productService.listInactiveProducts();
        if (inactive.isEmpty()) {
            System.out.println("Det finns inga inaktiva produkter.");
            return;
        }

        System.out.println("\n--- INAKTIVA PRODUKTER ---");
        for (Product p : inactive) {
            System.out.println(p.getSku() + " - " + p.getName());
        }

        String sku = input.getString("\nAnge SKU att aktivera: ");
        Optional<Product> product = productService.getProductBySku(sku);

        if (product.isPresent()) {
            Product p = product.get();
            if (p.isActive()) {
                System.out.println("Produkten är redan aktiv.");
                return;
            }
            p.setActive(true);
            productService.updateProduct(p);
            System.out.println("Produkt aktiverad!");
        } else {
            System.out.println("Produkt hittades inte.");
        }
    }
}