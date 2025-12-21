package com.example.ecommerce.menu;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductMenu {
    private final CartMenu cartMenu;
    private final CartService cartService;
    private final ProductService productService;
    private final CustomerMenu customerMenu;
    private final Scanner scanner = new Scanner(System.in);


    public ProductMenu(CartMenu cartMenu, CartService cartService, ProductService productService, CustomerMenu customerMenu) {
        this.cartMenu = cartMenu;
        this.cartService = cartService;
        this.productService = productService;
        this.customerMenu = customerMenu;
    }

    public void show(Customer customer) {

        List<Product> products = productService.listActiveProducts();

        if (products.isEmpty()) {
            System.out.println("Inga produkter tillgängliga");
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println("\n=== PRODUKTER ===");

            int index = 1;
            for (Product p : products) {
                System.out.printf(
                        "%d. %s - %s kr%n",
                        index++, p.getName(), p.getPrice()
                );
            }

            System.out.println("0. Tillbaka");
            System.out.print("Välj produkt: ");

            String choice = scanner.nextLine();

            if ("0".equals(choice)) {
                return;
            }

            try {
                int selectedIndex = Integer.parseInt(choice) - 1;
                Product selectedProduct = products.get(selectedIndex);

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
                                return;
                            }

                            cartService.addProduct(customer, selectedProduct, qty);
                            System.out.println("Produkten lades i varukorgen!");
                        } catch (NumberFormatException e) {
                            System.out.println("Du måste ange ett heltal för antal");
                        }
                        break;

                    case "2":
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