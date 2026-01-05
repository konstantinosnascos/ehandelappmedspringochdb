package com.example.ecommerce.web;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.model.PaymentMethod;
import com.example.ecommerce.model.PaymentStatus;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
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
}