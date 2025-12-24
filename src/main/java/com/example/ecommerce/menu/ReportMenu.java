package com.example.ecommerce.menu;

import com.example.ecommerce.model.Inventory;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ReportService;
import com.example.ecommerce.repository.InventoryRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

@Component
public class ReportMenu {

    private final ReportService reportService;
    private final InventoryRepository inventoryRepository;
    private final Scanner scanner = new Scanner(System.in);

    public ReportMenu(ReportService reportService, InventoryRepository inventoryRepository) {
        this.reportService = reportService;
        this.inventoryRepository = inventoryRepository;
    }

    public void show() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== RAPPORTER ===");
            System.out.println("1. Top 5 bästsäljare");
            System.out.println("2. Produkter med lågt lager");
            System.out.println("3. Omsättning per dag");
            System.out.println("0. Tillbaka");
            System.out.print("Val: ");

            switch (scanner.nextLine()) {

                case "1":
                    reportService.getTopSellingProducts(5)
                            .forEach(row ->
                                    System.out.println(row[0] + " – sålda: " + row[1]));
                    break;

                case "2":
                    System.out.print("Gräns för lågt lager (< X): ");
                    int threshold = Integer.parseInt(scanner.nextLine());

                    for (Product p : reportService.getLowStockProducts(threshold)) {
                        Inventory inv = inventoryRepository.findById(p.getId()).orElse(null);
                        int stock = (inv != null) ? inv.getInStock() : 0;

                        System.out.println(p.getName() + " – i lager: " + stock);
                    }
                    break;

                case "3":
                    System.out.print("Datum (YYYY-MM-DD): ");
                    LocalDate date = LocalDate.parse(scanner.nextLine());

                    LocalDateTime start = date.atStartOfDay();
                    LocalDateTime end = start.plusDays(1);

                    BigDecimal revenue = reportService.getRevenueBetween(start, end);
                    System.out.println("Omsättning: " + revenue + " kr");
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Ogiltigt val");
            }
        }
    }
}
