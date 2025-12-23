package com.example.ecommerce.menu;

import com.example.ecommerce.helper.InputHelper;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CartMenu {
    private static final Logger logger = LoggerFactory.getLogger(CartMenu.class);
    private final InputHelper input;
    private final CartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;
    private Customer currentCustomer;

    public CartMenu(InputHelper input, CartService cartService,
                    ProductService productService, CustomerService customerService) {
        this.input = input;
        this.cartService = cartService;
        this.productService = productService;
        this.customerService = customerService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            try {
                printMenu();
                int choice = input.getInt("Välj alternativ: ");
                switch (choice) {
                    case 1 -> selectCustomer();
                    case 2 -> addToCart();
                    case 3 -> showCart();
                    case 4 -> removeProduct();
                    case 5 -> clearCart();
                    case 6 -> running = false;
                    default -> System.out.println("Ogiltigt val, försök igen!");
                }
            } catch (Exception e) {
                System.out.println("Ett fel uppstod: " + e.getMessage());
                logger.error("Fel i CartMenu", e);
            }
        }
    }

    private void removeProduct() {
        if (currentCustomer == null) {
            System.out.println("Ingen kund vald.");
            return;
        }
        showCart();
        String sku = input.getString("Ange SKU att ta bort: ");
        try {
            cartService.removeProduct(currentCustomer, sku);
            System.out.println("Varan borttagen.");
        } catch (Exception e) {
            System.out.println("Fel: " + e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println("\n=== KUNDVAGN ===");
        if (currentCustomer != null) {
            System.out.println("Aktiv kund: " + currentCustomer.getName() + " (" + currentCustomer.getEmail() + ")");
        } else {
            System.out.println("Ingen kund vald");
        }
        System.out.println("1. Välj kund (Logga in)");
        System.out.println("2. Lägg till produkt");
        System.out.println("3. Visa kundvagn");
        System.out.println("4. Ta bort en vara (SKU)");
        System.out.println("5. Töm kundvagn");
        System.out.println("6. Tillbaka till huvudmeny");
    }

    private void selectCustomer() {
        String email = input.getString("Ange kundens email: ");
        try {
            Customer customer = customerService.createCustomer(email, "Placeholder");
            if (customer.getName().equals("Placeholder")) {
                String name = input.getString("Ny kund! Ange namn: ");
                customer.setName(name);
                customerService.createCustomer(email, name);
            }
            this.currentCustomer = customer;
            System.out.println("Inloggad som: " + currentCustomer.getName());
        } catch (Exception e) {
            System.out.println("Kunde inte välja kund: " + e.getMessage());
        }
    }

    private void addToCart() {
        if (currentCustomer == null) {
            System.out.println("Du måste välja en kund först!");
            return;
        }

        String sku = input.getString("Ange SKU: ");
        Optional<Product> productOpt = productService.getProductBySku(sku);

        if (productOpt.isEmpty()) {
            System.out.println("Produkt hittades inte.");
            return;
        }

        Product product = productOpt.get();
        if (!product.isActive()) {
            System.out.println("Produkten är inte aktiv.");
            return;
        }

        System.out.println("Vald produkt: " + product.getName() + " - " + product.getPrice() + " kr");
        int qty = input.getInt("Antal: ");

        if (qty > 0) {
            try {
                cartService.addProduct(currentCustomer, product, qty);
                System.out.println("Produkt tillagd i varukorgen.");
            } catch (Exception e) {
                System.out.println("Kunde inte lägga till: " + e.getMessage());
            }
        }
    }

    private void showCart() {
        if (currentCustomer == null) {
            System.out.println("Du måste välja en kund först!");
            return;
        }

        try {
            Cart cart = cartService.getCartWithItems(currentCustomer);
            if (cart.getItems().isEmpty()) {
                System.out.println("Kundvagnen är tom.");
                return;
            }

            System.out.println("\n=== INNEHÅLL ===");
            BigDecimal total = BigDecimal.ZERO;
            for (CartItem item : cart.getItems()) {
                BigDecimal lineTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQty()));
                total = total.add(lineTotal);
                System.out.printf("%s | Antal: %d | Pris: %s kr | Totalt: %s kr%n",
                        item.getProduct().getName(), item.getQty(), item.getProduct().getPrice(), lineTotal);
            }
            System.out.println("Summa totalt: " + total + " kr");
        } catch (Exception e) {
            System.out.println("Kunde inte visa varukorg: " + e.getMessage());
        }
    }

    private void clearCart() {
        if (currentCustomer == null) {
            System.out.println("Du måste välja en kund först!");
            return;
        }
        try {
            Cart cart = cartService.getCartWithItems(currentCustomer);
            cartService.clearCart(cart);
            System.out.println("Varukorgen tömd.");
        } catch (Exception e) {
            System.out.println("Fel vid tömning: " + e.getMessage());
        }
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}