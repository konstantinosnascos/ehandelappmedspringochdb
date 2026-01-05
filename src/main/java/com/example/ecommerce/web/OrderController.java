package com.example.ecommerce.web;

import com.example.ecommerce.model.*;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, PaymentService paymentService, CustomerService customerService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);

        paymentService.getPaymentByOrder(order)
                .ifPresent(payment -> model.addAttribute("payment", payment));

        model.addAttribute("order", order);
        model.addAttribute("methods", PaymentMethod.values());

        return "order";
    }

    @PostMapping("/{id}/pay")
    public String payOrder(
            @PathVariable Long id,
            @RequestParam PaymentMethod method
    ) {
        Order order = orderService.getOrderById(id);
        Payment payment = paymentService.processingPayment(order, method);

        if (payment.getStatus() == PaymentStatus.APPROVED) {
            orderService.markAsPaid(order.getId());
        }

        return "redirect:/orders/" + id;
    }

    @GetMapping
    public String listCustomerOrders(HttpSession session, Model model) {
        String email = (String) session.getAttribute("customerEmail");

        if (email == null) {
            throw new IllegalStateException("Ingen kund vald");
        }

        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Kund finns inte"));

        model.addAttribute("orders", orderService.getCustomerOrders(customer));

        return "orders";
    }
}