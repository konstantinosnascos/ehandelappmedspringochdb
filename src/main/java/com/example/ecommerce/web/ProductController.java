package com.example.ecommerce.web;

import com.example.ecommerce.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.AdminProductService;
import com.example.ecommerce.service.CategoryService;
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
    private final CategoryService categoryService;

    public ProductController(ProductService productService,
                             AdminProductService adminProductService,
                             InventoryService inventoryService,
                             CategoryService categoryService) {
        this.productService = productService;
        this.adminProductService = adminProductService;
        this.inventoryService = inventoryService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<Product> activeProducts =
                productService.listActiveProducts(
                        PageRequest.of(page, size)
                );

        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("inactiveProducts", productService.listInactiveProducts());
        model.addAttribute("inventoryService", inventoryService);
        model.addAttribute("categories", categoryService.getAllCategories());

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

    @PostMapping("/activate")
    public String activateProduct(@RequestParam Long productId) {
        adminProductService.activateProduct(productId);
        return "redirect:/products";
    }

    @PostMapping("/add-category")
    public String addCategoryToProduct(
            @RequestParam Long productId,
            @RequestParam Long categoryId
    ) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produkt finns inte"));
        Category category = categoryService.getCategoriyViaId(categoryId)
                .orElseThrow();

        product.getCategories().add(category);
        productService.updateProduct(product);

        return "redirect:/products";
    }

    @GetMapping("/category/{name}")
    public String productsByCategory(
            @PathVariable String name,
            Model model
    ) {
        model.addAttribute("products", productService.getProductsByCategory(name));
        model.addAttribute("category", name);
        return "products-by-category";
    }
}