package com.example.ecommerce.menu;

import com.example.ecommerce.helper.InputHelper;
import com.example.ecommerce.service.CSVReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SystemMenu {
    private static final Logger logger = LoggerFactory.getLogger(SystemMenu.class);

    private final InputHelper input;
    private final CSVReaderService csvReaderService;

    public SystemMenu(InputHelper input, CSVReaderService csvReaderService) {
        this.input = input;
        this.csvReaderService = csvReaderService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            try {
                printMenu();
                int choice = input.getInt("Välj alternativ: ");
                switch (choice) {
                    case 1 -> csvReaderService.importFromMenu();
                    case 2 -> running = false;
                    default -> System.out.println("Ogiltigt val.");
                }
            } catch (Exception e) {
                System.out.println("Fel: " + e.getMessage());
                logger.error("Fel i SystemMenu", e);
            }
        }
    }


    private void printMenu() {
        System.out.println("\n=== SYSTEM & IMPORT ===");
        System.out.println("1. Importera CSV från 'incoming'-mappen");
        System.out.println("2. Tillbaka");
    }
}