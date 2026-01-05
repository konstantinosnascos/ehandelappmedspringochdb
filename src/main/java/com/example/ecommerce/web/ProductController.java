package com.example.ecommerce.web;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.AdminProductService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final AdminProductService adminProductService;
    private final InventoryService inventoryService;

    public ProductController(ProductService productService,
                             AdminProductService adminProductService,
                             InventoryService inventoryService) {
        this.productService = productService;
        this.adminProductService = adminProductService;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.listActiveProducts());
        model.addAttribute("inventoryService", inventoryService);
        return "products";
    }

    @PostMapping
    public String createProduct(
            @RequestParam String sku,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam int stock
    ) {
        adminProductService.createProduct(sku, name, description, price, stock);
        return "redirect:/products";
    }

    @PostMapping("/deactivate")
    public String deactivateProduct(@RequestParam Long productId) {
        adminProductService.deactivateProduct(productId);
        return "redirect:/products";
    }
}