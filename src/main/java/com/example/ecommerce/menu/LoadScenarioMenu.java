package com.example.ecommerce.menu;

import com.example.ecommerce.service.CSVReaderService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

@Component
public class LoadScenarioMenu
{
    private final CSVReaderService csvReaderService;

    LoadScenarioMenu(CSVReaderService csvReaderService)
    {
        this.csvReaderService = csvReaderService;
    }

    public void show(Scanner userInput) throws IOException {
        System.out.println("\n=== Scenario Menu ===");
        System.out.println("1. Ladda Simpelt Scenario ");
        System.out.println("2. Ladda Mellan Scenario");
        System.out.println("3. Ladda Stort Scenario");
        System.out.println("0. Tillbaka");

        try
        {
            switch (userInput.nextLine())
            {
                case "1":
                    loadSimpleScenario();
                    break;

                case "2":
                    loadMediumScenario();
                    break;

                case "3":
                    loadLargeScenario();
                    break;

                default:
                    System.out.println("Ogiltigt val");
                    break;
            }

            System.out.println("Scenario uppladdat");
        }
        catch (Exception e)
        {
            System.out.println("Fel vid import: " + e.getMessage());
        }
    }

    private void loadSimpleScenario() throws IOException
    {
        Path pathProductsSimple = Path.of("data/simple/products.csv");
        Path pathCustomersSimple = Path.of("data/simple/customers.csv");
        Path pathCategoriesSimple = Path.of("data/simple/categories.csv");

        csvReaderService.products(pathProductsSimple);
        csvReaderService.customers(pathCustomersSimple);
        csvReaderService.categories(pathCategoriesSimple);
    }

    private void loadMediumScenario() throws IOException
    {
        Path pathProductsMedium = Path.of("data/medium/products.csv");
        Path pathCustomersMedium = Path.of("data/medium/customers.csv");
        Path pathCategoriesMedium = Path.of("data/medium/categories.csv");

        csvReaderService.products(pathProductsMedium);
        csvReaderService.customers(pathCustomersMedium);
        csvReaderService.categories(pathCategoriesMedium);
    }

    private void loadLargeScenario() throws IOException
    {
        Path pathProductsLarge = Path.of("data/large/products.csv");
        Path pathCustomersLarge = Path.of("data/large/customers.csv");
        Path pathCategoriesLarge = Path.of("data/large/categories.csv");

        csvReaderService.products(pathProductsLarge);
        csvReaderService.customers(pathCustomersLarge);
        csvReaderService.categories(pathCategoriesLarge);
    }
}
