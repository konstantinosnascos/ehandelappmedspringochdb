package com.example.ecommerce.menu;

import com.example.ecommerce.helper.InputHelper;
import com.example.ecommerce.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportMenu {
    private static final Logger logger = LoggerFactory.getLogger(ReportMenu.class);
    private final InputHelper input;
    private final ReportService reportService;

    public ReportMenu(InputHelper input, ReportService reportService) {
        this.input = input;
        this.reportService = reportService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            try {
                printMenu();
                int choice = input.getInt("Välj alternativ: ");
                switch (choice) {
                    case 1 -> topProducts();
                    case 2 -> running = false;
                    default -> System.out.println("Ogiltigt val.");
                }
            } catch (Exception e) {
                System.out.println("Fel: " + e.getMessage());
                logger.error("Fel i ReportMenu", e);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== RAPPORTER ===");
        System.out.println("1. Visa bästsäljare");
        System.out.println("2. Tillbaka");
    }

    private void topProducts() {
        int limit = input.getInt("Hur många produkter? ");
        List<Object[]> results = reportService.getTopSellingProducts(limit);
        System.out.println("--- TOPPLISTA ---");
        for (Object[] row : results) {
            System.out.println(row[0] + " - Antal sålda: " + row[1]);
        }
    }
}
