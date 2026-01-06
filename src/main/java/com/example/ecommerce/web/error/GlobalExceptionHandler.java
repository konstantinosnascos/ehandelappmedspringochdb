package com.example.ecommerce.web.error;

import com.example.ecommerce.exception.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Något gick fel");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(
            ProductNotFoundException ex,
            Model model
    ) {
        model.addAttribute("errorTitle", "Produkt saknas");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InsufficientStockException.class)
    public String handleStockError(
            InsufficientStockException ex,
            Model model
    ) {
        model.addAttribute("errorTitle", "Otillräckligt lager");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFound(
            OrderNotFoundException ex,
            Model model
    ) {
        model.addAttribute("errorTitle", "Order saknas");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(
            IllegalStateException ex,
            Model model
    ) {
        model.addAttribute("errorTitle", "Ogiltigt tillstånd");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}