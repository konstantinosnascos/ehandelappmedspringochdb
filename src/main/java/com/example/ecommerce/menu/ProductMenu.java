package com.example.ecommerce.menu;

import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.InventoryService;
import com.example.ecommerce.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductMenu {
    private final CartMenu cartMenu;
    private final CustomerMenu customerMenu;
    private final CartService cartService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final InventoryService inventoryService;


    private final Scanner scanner = new Scanner(System.in);

    public ProductMenu(CartMenu cartMenu, CustomerMenu customerMenu, CartService cartService, ProductService productService, CategoryService categoryService, InventoryService inventoryService) {
        this.cartMenu = cartMenu;
        this.customerMenu = customerMenu;
        this.cartService = cartService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.inventoryService = inventoryService;
    }

    public void show(Customer customer) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== PRODUKTKATALOG ===");
            System.out.println("1. Visa alla produkter");
            System.out.println("2. Visa produkter efter kategori");
            System.out.println("3. Visa produkter efter namn");
            System.out.println("0. Tillbaka");
            System.out.print("Val: ");

            List<Product> productsToShow = new ArrayList<>();

            int userInput = Integer.parseInt(scanner.nextLine());

            //Första val
            switch (userInput) {
                case 1:
                    productsToShow = productService.listActiveProducts();
                    break;

                case 2:
                    List<String> categoryNames = categoryService.getAllCategories()
                            .stream()
                            .map(Category::getName)
                            .toList();

                    if (categoryNames.isEmpty()) {
                        System.out.println("Inga kategorier finns.");
                        break;
                    }

                    System.out.println("Tillgängliga kategorier:");
                    for (int i = 0; i < categoryNames.size(); i++) {
                        System.out.printf("%d. %s%n", i + 1, categoryNames.get(i));
                    }

                    System.out.print("Välj kategori (nummer): ");
                    try {
                        int catChoice = Integer.parseInt(scanner.nextLine()) - 1;
                        String selectedCategory = categoryNames.get(catChoice).trim();
                        productsToShow = filterProductsByCategory(selectedCategory);
                    } catch (Exception e) {
                        System.out.println("Ogiltigt kategorival." + e.getMessage());
                        return;
                    }
                    break;

                case 3:
                    System.out.print("Sök produktnamn: ");
                    String keyword = scanner.nextLine();

                    productsToShow = productService.searchProductsByName(keyword);

                    if (productsToShow.isEmpty()) {
                        System.out.println("Inga produkter hittades.");
                    }
                    break;

                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            boolean browsingProducts = true;

            while (browsingProducts) {
                System.out.println("\n=== PRODUKTER ===");


                int index = 1;
                for (Product p : productsToShow) {

                    int stock = inventoryService.getStockLevel(p.getId());
                    if (stock == 0) {
                        System.out.printf(
                                "%d. %s - %s kr (SLUT I LAGER)%n",
                                index++, p.getName(), p.getPrice()
                        );
                    } else {
                        System.out.printf(
                                "%d. %s - %s kr (i lager: %d st)%n",
                                index++, p.getName(), p.getPrice(), stock
                        );
                    }
                }

                System.out.println("0. Tillbaka");
                System.out.print("Välj produkt: ");

                String choice = scanner.nextLine();

                //Andra val
                if ("0".equals(choice)) {
                    return;
                }

                try {
                    int selectedIndex = Integer.parseInt(choice) - 1;
                    Product selectedProduct = productsToShow.get(selectedIndex);

                    System.out.println("\nVald produkt: " + selectedProduct.getName());
                    System.out.println("1. Lägg i varukorg");
                    System.out.println("2. Visa varukorg");
                    System.out.println("0. Tillbaka");
                    System.out.print("Val: ");

                    String action = scanner.nextLine();

                    switch (action) {
                        case "1":
                            System.out.print("Antal: ");
                            String qtyInput = scanner.nextLine();

                            try {
                                int qty = Integer.parseInt(qtyInput);

                                if (qty <= 0) {
                                    System.out.println("Antal måste vara större än 0");
                                    break;
                                }

                                if (customer == null) {
                                    customer = customerMenu.createCustomer();
                                }

                                try {
                                    cartService.addProduct(customer, selectedProduct, qty);
                                    System.out.println("Produkten lades i varukorgen!");

                                } catch (InsufficientStockException e) {
                                    System.out.println(" " + e.getMessage());

                                } catch (ProductNotFoundException e) {
                                    System.out.println("Produkten finns inte längre");

                                }

                            } catch (NumberFormatException e) {
                                System.out.println("Du måste ange ett heltal för antal");
                            }
                            break;

                        case "2":
                            if(customer == null)
                            {
                                customer = customerMenu.createCustomer();
                            }
                            cartMenu.show(customer);
                            break;
                        case "0":
                            return;

                        default:
                            System.out.println("Ogiltigt val");
                    }

                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Ogiltigt produktval");
                }
            }
        }
    }

    private List<Product> filterProductsByCategory(String categoryName) {

        List<Product> productsList = productService.getProductsByCategory(categoryName);

        if (productsList.isEmpty()) {
            System.out.println("Inga produkter hittades i kategorin " + categoryName);
            return new ArrayList<>();
        }

        System.out.println("Hittade: " + productsList.size() + " Produkter i kategorin");

        return productsList;
    }
}