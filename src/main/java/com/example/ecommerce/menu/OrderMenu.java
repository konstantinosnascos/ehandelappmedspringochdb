package com.example.ecommerce.menu;

import com.example.ecommerce.helper.InputHelper;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.*;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.PaymentService;
import com.example.ecommerce.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
@Component
public class OrderMenu {
    private static final Logger logger = LoggerFactory.getLogger(OrderMenu.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final InputHelper input;
    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final CartMenu cartMenu;

    public OrderMenu(InputHelper input, OrderService orderService,
                     CartService cartService, PaymentService paymentService,
                     InventoryService inventoryService, CartMenu cartMenu) {
        this.input = input;
        this.orderService = orderService;
        this.cartService = cartService;
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.cartMenu = cartMenu;
    }

    public void run() {
        boolean running = true;
        while (running) {
            try {
                printMenu();
                int choice = input.getInt("Välj alternativ: ");
                switch (choice) {
                    case 1 -> checkout();
                    case 2 -> listMyOrders();
                    case 3 -> showOrderDetails();
                    case 4 -> running = false;
                    default -> System.out.println("Ogiltigt val, försök igen!");
                }
            } catch (Exception e) {
                System.out.println("Ett fel uppstod: " + e.getMessage());
                logger.error("Fel i OrderMenu", e);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== ORDERHANTERING ===");
        System.out.println("1. Checka ut (Skapa order)");
        System.out.println("2. Mina ordrar (Historik)");
        System.out.println("3. Visa orderdetaljer (ID)");
        System.out.println("4. Tillbaka till huvudmeny");
    }

    private void checkout() {
        Customer customer = cartMenu.getCurrentCustomer();
        if (customer == null) {
            System.out.println("Du måste vara inloggad (Välj kund i Kundvagn-menyn).");
            return;
        }

        try {
            Cart cart = cartService.getCartWithItems(customer);
            if (cart.getItems().isEmpty()) {
                System.out.println("Varukorgen är tom.");
                return;
            }

            for (CartItem item : cart.getItems()) {
                if (!inventoryService.hasStock(item.getProduct().getId(), item.getQty())) {
                    System.out.println("Slut i lager för: " + item.getProduct().getName());
                    return;
                }
            }

            System.out.println("Välj betalmetod: 1. Kort, 2. Faktura");
            int methodChoice = input.getInt("Val: ");
            PaymentMethod method = (methodChoice == 2) ? PaymentMethod.INVOICE : PaymentMethod.CARD;

            for (CartItem item : cart.getItems()) {
                inventoryService.decrease(item.getProduct().getId(), item.getQty());
            }

            Order order = orderService.createOrderFromCart(cart);
            Payment payment = paymentService.processingPayment(order, method);

            if (payment.getStatus() == PaymentStatus.APPROVED) {
                orderService.markAsPaid(order.getId());
                cartService.clearCart(cart);
                System.out.println("Betalning godkänd! Order ID: " + order.getId());
            } else {
                orderService.cancelOrder(order.getId());
                for (CartItem item : cart.getItems()) {
                    inventoryService.increase(item.getProduct().getId(), item.getQty());
                }
                System.out.println("Betalning nekad. Order avbruten.");
            }

        } catch (Exception e) {
            System.out.println("Fel vid checkout: " + e.getMessage());
        }
    }

    private void listMyOrders() {
        Customer customer = cartMenu.getCurrentCustomer();
        if (customer == null) {
            System.out.println("Ingen kund vald. Logga in via Kundvagn-menyn först.");
            return;
        }

        List<Order> orders = orderService.getCustomerOrders(customer);
        if (orders.isEmpty()) {
            System.out.println("Inga tidigare ordrar hittades.");
            return;
        }

        System.out.println("\n--- ORDERHISTORIK: " + customer.getName() + " ---");
        System.out.printf("%-5s | %-16s | %-10s | %s%n", "ID", "Datum", "Status", "Belopp");
        System.out.println("------------------------------------------------");

        for (Order o : orders) {
            System.out.printf("#%-4d | %-16s | %-10s | %s kr%n",
                    o.getId(),
                    o.getCreatedAt().format(formatter),
                    o.getStatus(),
                    o.getTotal());
        }
    }

    private void showOrderDetails() {

        int id = input.getInt("Ange Order ID: ");
        try {
            Order order = orderService.getOrderById((long) id);

            System.out.println("\n=== ORDER #" + order.getId() + " ===");
            System.out.println("Kund: " + order.getCustomer().getName());
            System.out.println("Datum: " + order.getCreatedAt().format(formatter));
            System.out.println("Status: " + order.getStatus());
            System.out.println("\nProdukter:");

            for (OrderItem item : order.getItems()) {
                System.out.printf("- %s (x%d) á %s kr = %s kr%n",
                        item.getProduct().getName(),
                        item.getQty(),
                        item.getUnitPrice(),
                        item.getLineTotal());
            }
            System.out.println("-----------------------------");
            System.out.println("TOTALT: " + order.getTotal() + " kr");

        } catch (Exception e) {
            System.out.println("Kunde inte hämta order: " + e.getMessage());
        }
    }
}