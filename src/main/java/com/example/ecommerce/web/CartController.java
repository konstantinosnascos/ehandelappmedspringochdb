package com.example.ecommerce.web;

import com.example.ecommerce.model.*;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CustomerService customerService;
    private final ProductService productService;

    public CartController(CartService cartService,
                          CustomerService customerService,
                          ProductService productService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Customer customer = getCustomerFromSession(session);
        Cart cart = cartService.getCartWithItems(customer);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam String sku,
            @RequestParam int qty,
            HttpSession session
    ) {
        Customer customer = getCustomerFromSession(session);
        Product product = productService.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Produkt finns inte"));

        cartService.addProduct(customer, product, qty);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(
            @RequestParam String sku,
            HttpSession session
    ) {
        Customer customer = getCustomerFromSession(session);
        cartService.removeProduct(customer, sku);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        Customer customer = getCustomerFromSession(session);
        Cart cart = cartService.getOrCreateCart(customer);
        cartService.clearCart(cart);
        return "redirect:/cart";
    }

    private Customer getCustomerFromSession(HttpSession session) {
        String email = (String) session.getAttribute("customerEmail");

        if (email == null) {
            throw new IllegalStateException("Ingen kund vald");
        }

        return customerService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Kund finns inte"));
    }
}