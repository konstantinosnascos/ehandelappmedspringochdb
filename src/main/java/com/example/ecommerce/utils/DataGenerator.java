package com.example.ecommerce.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private static final int NUM_CUSTOMERS = 500;
    private static final int NUM_PRODUCTS = 1000;
    private static final int NUM_ORDERS = 2000;

    private static final String DIR = "incoming/";
    private static final Random random = new Random();

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(DIR));

            System.out.println("Genererar data...");

            List<String> emails = generateCustomers();
            List<String> skus = generateProducts();
            generateOrders(emails, skus);

            System.out.println("KLART! Filer skapade i 'incoming/':");
            System.out.println("- large_customers.csv (" + NUM_CUSTOMERS + " st)");
            System.out.println("- large_products.csv (" + NUM_PRODUCTS + " st)");
            System.out.println("- large_orders.csv (" + NUM_ORDERS + " st)");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Ni behöver inte köra denna. Det är redan klart
    private static List<String> generateCustomers() throws IOException {
        List<String> emails = new ArrayList<>();
        try (FileWriter writer = new FileWriter(DIR + "large_customers.csv")) {
            writer.write("email,name\n");

            String[] firstNames = {"Anna", "Erik", "Lars", "Karin", "Maria", "Johan", "Per", "Eva", "Karl", "Kristina"};
            String[] lastNames = {"Svensson", "Johansson", "Andersson", "Nilsson", "Larsson", "Olsson", "Persson"};

            for (int i = 1; i <= NUM_CUSTOMERS; i++) {
                String fname = firstNames[random.nextInt(firstNames.length)];
                String lname = lastNames[random.nextInt(lastNames.length)];
                String email = "user" + i + "@example.com"; // Unika emails

                writer.write(email + "," + fname + " " + lname + "\n");
                emails.add(email);
            }
        }
        return emails;
    }

    private static List<String> generateProducts() throws IOException {
        List<String> skus = new ArrayList<>();
        try (FileWriter writer = new FileWriter(DIR + "large_products.csv")) {
            writer.write("sku,name,description,price,stock\n");

            String[] adjectives = {"Super", "Mega", "Ultra", "Billig", "Lyxig", "Smart", "Snabb"};
            String[] nouns = {"Telefon", "Dator", "Skärm", "Mus", "Kabel", "Laddare", "Hörlur"};

            for (int i = 1; i <= NUM_PRODUCTS; i++) {
                String sku = "PROD-" + i;
                String name = adjectives[random.nextInt(adjectives.length)] + " " + nouns[random.nextInt(nouns.length)] + " " + i;
                double price = 10 + random.nextInt(9990); // Pris mellan 10 och 10000
                int stock = random.nextInt(100) + 10; // Lager mellan 10 och 110

                writer.write(sku + "," + name + ",Beskrivning av " + name + "," + price + "," + stock + "\n");
                skus.add(sku);
            }
        }
        return skus;
    }

    private static void generateOrders(List<String> emails, List<String> skus) throws IOException {
        try (FileWriter writer = new FileWriter(DIR + "large_orders.csv")) {
            writer.write("email,status,date,items\n");

            String[] statuses = {"PAID", "PAID", "PAID", "PAID", "NEW", "CANCELLED"}; // Mest PAID

            for (int i = 0; i < NUM_ORDERS; i++) {
                String email = emails.get(random.nextInt(emails.size()));
                String status = statuses[random.nextInt(statuses.length)];

                LocalDateTime date = LocalDateTime.of(2023, 1, 1, 10, 0)
                        .plusDays(random.nextInt(365 * 2))
                        .plusHours(random.nextInt(12));

                int numItems = random.nextInt(4) + 1; // 1 till 4 produkter per order
                StringBuilder itemsStr = new StringBuilder();

                for (int j = 0; j < numItems; j++) {
                    String sku = skus.get(random.nextInt(skus.size()));
                    int qty = random.nextInt(3) + 1; // 1-3 st

                    if (j > 0) itemsStr.append("|");
                    itemsStr.append(sku).append(":").append(qty);
                }

                writer.write(email + "," + status + "," + date.toString() + "," + itemsStr.toString() + "\n");
            }
        }
    }
}