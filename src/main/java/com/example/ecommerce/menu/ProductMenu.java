package com.example.ecommerce.menu;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductMenu {

    private final ProductService productService;
    private final Scanner scanner = new Scanner(System.in);

    public ProductMenu(ProductService productService) {
        this.productService = productService;
    }

    public void show()
    {
        List<Product> products = productService.listActiveProducts();

        System.out.println("\n=== PRODUKTER ===");

        if(products.isEmpty())
        {
            System.out.println("Inga produkter tillgängliga");
            return;
        }

        int index = 1;
        for (Product p : products) {
            System.out.printf(
                    "%d. %s - %s kr%n",
                    index++, p.getName(), p.getPrice());
        }

        System.out.println("0. Tillbaka");
        System.out.print("Välj produkt:");

        String choice = scanner.nextLine();

        if("0".equals(choice))
        {
            return;
        }

        try{
            int selectedIndex = Integer.parseInt(choice) -1;
            Product selectedProduct = products.get(selectedIndex);

            System.out.println("Du valde: " + selectedProduct.getName());


        } catch (Exception e)
        {
            System.out.println("Ogiltigt val");
        }
    }


}
