package com.example.ecommerce.menu;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class OrderMenu {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final Scanner scanner = new Scanner(System.in);

    public OrderMenu(OrderService orderService,
                     OrderRepository orderRepository,
                     CartService cartService) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    @Transactional
    public void showMenu() {
        while (true) {
            System.out.println("\n=== ORDERHANTERING ===");
            System.out.println("1. Visa alla ordrar");
            System.out.println("2. Visa order via ID");
            System.out.println("3. Visa ordrar via status");
            System.out.println("4. Skapa order från varukorg");
            System.out.println("5. Avbryt order");
            System.out.println("6. Markera order som BETALD");
            System.out.println("0. Tillbaka");
            System.out.print("Välj ett alternativ: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> ShowAllOrders();
                case 2 -> ShowOrdersById();
                case 3 -> ShowOrdersByStatus();
                case 4 -> CreateOrderFromCart();
                case 5 -> CancelOrder();
                case 6 -> MarkOrderAsPaid();
                case 0 -> {
                    System.out.println("Återgår till huvudmenyn.");
                    return;
                }
                default -> System.out.println("Ogiltigt val. Försök igen.");
            }
        }
    }

    private void ShowAllOrders() {
        List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            System.out.println("Inga ordrar hittades.");
        } else {
            orders.forEach(this::printOrder);
        }
    }

    private void ShowOrdersById() {
        System.out.print("Ange order-ID: ");
        Long orderId = Long.parseLong(scanner.nextLine());

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        orderOptional.ifPresentOrElse(
                this::printOrder,
                () -> System.out.println("Order hittades inte.")
        );
    }

    private void ShowOrdersByStatus() {
        System.out.print("Ange orderstatus (CREATED, PAID, CANCELLED): ");
        String statusStr = scanner.nextLine().toUpperCase();

        OrderStatus status;
        try {
            status = OrderStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Ogiltig status.");
            return;
        }

        List<Order> orders = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == status)
                .toList();

        if (orders.isEmpty()) {
            System.out.println("Inga ordrar med status " + status + " hittades.");
        } else {
            orders.forEach(this::printOrder);
        }
    }

    private void CreateOrderFromCart() {
        System.out.print("Ange kund-ID: ");
        Long customerId = Long.parseLong(scanner.nextLine());

        Customer customer = new Customer();
        customer.setId(customerId);

        try {
            Cart cart = cartService.getCartWithItems(customer);

            if (cart.getItems().isEmpty()) {
                System.out.println("Varukorgen är tom.");
                return;
            }

            Order order = orderService.createOrderFromCart(cart);
            cartService.clearCart(cart);

            System.out.println("Order skapad!");
            System.out.println("Order-ID: " + order.getId());
            printOrder(order);

        } catch (Exception e) {
            System.out.println("Fel vid skapande av order: " + e.getMessage());
        }
    }

    private void CancelOrder() {
        System.out.print("Ange order-ID: ");
        Long orderId = Long.parseLong(scanner.nextLine());

        try {
            orderService.cancelOrder(orderId);
            System.out.println("Order " + orderId + " har avbrutits.");
        } catch (Exception e) {
            System.out.println("Fel vid avbrytning: " + e.getMessage());
        }
    }

    private void MarkOrderAsPaid() {
        System.out.print("Ange order-ID: ");
        Long orderId = Long.parseLong(scanner.nextLine());

        try {
            orderService.markAsPaid(orderId);
            System.out.println("Order " + orderId + " är nu markerad som BETALD.");
        } catch (Exception e) {
            System.out.println("Fel vid betalning: " + e.getMessage());
        }
    }

    private void printOrder(Order order) {
        System.out.println("\nOrder-ID: " + order.getId());
        System.out.println("Kund-ID: " + order.getCustomer().getId());
        System.out.println("Status: " + order.getStatus());
        System.out.println("Totalbelopp: " + order.getTotal() + " kr");
        System.out.println("Skapad: " + order.getCreatedAt());
        System.out.println("Produkter:");

        order.getItems().forEach(item ->
                System.out.println(
                        " - " + item.getProduct().getName() +
                                " x" + item.getQty() +
                                " = " + item.getLineTotal() + " kr"
                )
        );
    }
}
