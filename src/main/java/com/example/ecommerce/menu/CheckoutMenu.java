package com.example.ecommerce.menu;

import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.InventoryService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.PaymentService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CheckoutMenu {

    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final Scanner scanner = new Scanner(System.in);

    public CheckoutMenu(OrderService orderService, CartService cartService, PaymentService paymentService, InventoryService inventoryService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
    }

    public void checkout(Cart cart) {

        if (cart.getItems().isEmpty()) {
            System.out.println("Varukorgen är tom.");
            return;
        }

        System.out.println("\n=== CHECKOUT ===");
        System.out.println("Välj betalmetod:");
        System.out.println("1. Kort");
        System.out.println("2. Faktura");
        System.out.println("0. Avbryt");
        System.out.print("Ditt val: ");

        String choice = scanner.nextLine();

        PaymentMethod method = switch (choice) {
            case "1" -> PaymentMethod.CARD;
            case "2" -> PaymentMethod.INVOICE;
            default -> {
                System.out.println("Checkout avbruten");
                yield null;
            }
        };

        if (method == null) return;

        try {
            // ↓ ALL affärslogik i try
            for (CartItem item : cart.getItems()) {
                inventoryService.decrease(
                        item.getProduct().getId(),
                        item.getQty()
                );
            }

            Order order = orderService.createOrderFromCart(cart);
            Payment payment = paymentService.processingPayment(order, method);

            if (payment.getStatus() == PaymentStatus.APPROVED) {
                orderService.markAsPaid(order.getId());
                cartService.clearCart(cart);

                System.out.println("✅ Betalning godkänd!");
                System.out.println("Order-ID: " + order.getId());
                System.out.println("Total: " + order.getTotal() + " kr");
            } else {
                orderService.cancelOrder(order.getId());
                System.out.println("Betalning nekad");
            }

        } catch (InsufficientStockException e) {
            System.out.println("Köpet avbröts: " + e.getMessage());

        } catch (ProductNotFoundException e) {
            System.out.println("Köpet avbröts: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Tekniskt fel vid checkout");
        }
    }
}