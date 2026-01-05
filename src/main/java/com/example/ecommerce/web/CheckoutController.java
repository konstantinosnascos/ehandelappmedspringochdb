package com.example.ecommerce.web;

import com.example.ecommerce.model.*;
import com.example.ecommerce.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final CustomerService customerService;

    public CheckoutController(
            CartService cartService,
            OrderService orderService,
            CustomerService customerService
    ) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping
    public String checkoutPage(HttpSession session, Model model) {
        Customer customer = getCustomer(session);
        Cart cart = cartService.getCartWithItems(customer);

        model.addAttribute("cart", cart);
        model.addAttribute("paymentMethods", PaymentMethod.values());
        return "checkout";
    }

    @PostMapping
    public String placeOrder(HttpSession session) {
        Customer customer = getCustomer(session);
        Cart cart = cartService.getCartWithItems(customer);

        Order order = orderService.createOrderFromCart(cart);
        cartService.clearCart(cart);

        return "redirect:/orders/" + order.getId();
    }

    private Customer getCustomer(HttpSession session) {
        String email = (String) session.getAttribute("customerEmail");
        return customerService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Ingen kund vald"));
    }
}