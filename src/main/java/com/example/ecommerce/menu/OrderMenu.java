package com.example.ecommerce.menu;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class OrderMenu
{
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final Scanner scanner = new Scanner(System.in);

    public OrderMenu(OrderService orderService, OrderRepository orderRepository, CartService cartService)
    {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public void showMenu()
    {
        while (true)
        {
            System.out.println("\n=== Order Management menu===");
            System.out.println("1. Show all orders");
            System.out.println("2. Show orders by id");
            System.out.println("3. Show orders by status");
            System.out.println("4. Create order from cart");
            System.out.println("5. Cancel order");
            System.out.println("6. Mark order as PAID");
            System.out.println("0. EXIT");
            System.out.println("Select an option");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice)
            {
                case 1 -> ShowAllOrders();
                case 2 -> ShowOrdersById();
                case 3 -> ShowOrdersByStatus();
                case 4 -> CreateOrderFromCart();
                case 5 -> CancelOrder();
                case 6 -> MarkOrderAsPaid();
                case 0 ->
                {
                    System.out.println("Exiting menu");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again");
            }
        }
    }

    private void ShowAllOrders()
    {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty())
        {
            System.out.println("No orders found");
        }
        else
        {
            orders.forEach(this::printOrder);
        }
    }

    private void ShowOrdersById()
    {
        System.out.println("Enter order id: ");
        Long orderId = Long.parseLong(scanner.nextLine());
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        orderOptional.ifPresentOrElse(this::printOrder, () -> System.out.println("Order not found"));
    }

    private void ShowOrdersByStatus()
    {
        System.out.println("Enter order status: ");
        String statusstr = scanner.nextLine().toUpperCase();
        OrderStatus status;
        try
        {
            status = OrderStatus.valueOf(statusstr);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Invalid status");
            return;
        }

        List<Order> orders = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == status)
                .toList();

        if (orders.isEmpty())
        {
            System.out.println("No orders found with status " + status);
        }
        else
        {
            orders.forEach(this::printOrder);
        }
    }

    private void CreateOrderFromCart()
    {
        System.out.println("Enter customer id");
        Long customerId = Long.parseLong(scanner.nextLine());
        Customer customer = new Customer();
        customer.setId(customerId);

        try
        {
            Cart cart = cartService.getCartWithItems(customer);
            if (cart.getItems().isEmpty())
            {
                System.out.println("Cart is empty");
                return;
            }

            Order order = orderService.createOrderFromCart(cart);
            cartService.clearCart(cart);
            System.out.println("Order created successfully. Order id: " +order.getId());
            printOrder(order);
        }
        catch (Exception e)
        {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }

    private void CancelOrder()
    {
        System.out.println("Enter order id");
        Long orderId = Long.parseLong(scanner.nextLine());
        try
        {
            orderService.cancelOrder(orderId);
            System.out.println("Order " + orderId + " cancelled");
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void MarkOrderAsPaid()
    {
        System.out.println("Enter order id");
        Long orderId = Long.parseLong(scanner.nextLine());
        try
        {
            orderService.markAsPaid(orderId);
            System.out.println("Order " + orderId + " marked as PAID");
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printOrder(Order order)
    {
        System.out.println("\nOrder ID: " + order.getId());
        System.out.println("Customer ID: " + order.getCustomer().getId());
        System.out.println("Status: " + order.getStatus());
        System.out.println("Total: " + order.getTotal());
        System.out.println("Created at: " + order.getCreatedAt());
        System.out.println("Items:");
        order.getItems().forEach(item ->
                System.out.println("  - " + item.getProduct().getName() + " x" + item.getQty() + " = " + item.getLineTotal())
        );
    }
}