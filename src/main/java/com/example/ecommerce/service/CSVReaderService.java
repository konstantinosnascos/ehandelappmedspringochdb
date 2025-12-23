package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class CSVReaderService {
    private static final Logger logger = LoggerFactory.getLogger(CSVReaderService.class);

    private final ProductService productService;
    private final CustomerService customerService;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    public CSVReaderService(ProductService productService,
                            CustomerService customerService,
                            InventoryService inventoryService,
                            OrderService orderService) {
        this.productService = productService;
        this.customerService = customerService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    public void importFromMenu() {
        Path filePath = getFilePathFromUser();

        if (filePath == null) {
            return;
        }

        String fileName = filePath.getFileName().toString().toLowerCase();

        if (fileName.contains("product")) {
            importProducts(filePath);
        } else if (fileName.contains("customer")) {
            importCustomers(filePath);
        } else if (fileName.contains("inventory")) {
            importInventory(filePath);
        } else if (fileName.contains("order")) {
            importOrders(filePath);
        } else {
            System.out.println("Okänd filtyp. Filnamnet måste innehålla 'product', 'customer', 'inventory' eller 'order'.");
        }
    }

    private Path getFilePathFromUser() {
        Path incomingDir = Paths.get("incoming");
        List<Path> validFiles;

        if (!Files.exists(incomingDir)) {
            System.out.println("Mappen 'incoming/' saknades. Skapar den nu...");
            try {
                Files.createDirectories(incomingDir);
                System.out.println("Mapp skapad! Lägg dina CSV-filer i 'incoming'-mappen och försök igen.");
            } catch (IOException e) {
                logger.error("Kunde inte skapa mapp incoming/", e);
                return null;
            }
            return null;
        }

        try (Stream<Path> files = Files.list(incomingDir)) {
            validFiles = files
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".csv"))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            System.out.println("Kunde inte läsa mappen.");
            return null;
        }

        if (validFiles.isEmpty()) {
            System.out.println("Inga CSV-filer hittades i 'incoming/'.");
            return null;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- TILLGÄNGLIGA FILER ---");
        for (int i = 0; i < validFiles.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, validFiles.get(i).getFileName());
        }

        while (true) {
            System.out.printf("Välj fil (1-%d) eller 0 för att avbryta: ", validFiles.size());
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice == 0) return null;
                if (choice >= 1 && choice <= validFiles.size()) {
                    return validFiles.get(choice - 1);
                }
            } else {
                scanner.next();
            }
            System.out.println("Ogiltigt val.");
        }
    }

    @Transactional
    public void importProducts(Path filePath) {
        System.out.println("Importerar produkter...");
        int count = 0;
        int skipped = 0;

        try (Stream<String> lines = Files.lines(filePath)) {
            Iterator<String> it = lines.iterator();
            if (it.hasNext()) {
                String header = it.next();
                if (!header.toLowerCase().startsWith("sku")) {
                }
            }

            while (it.hasNext()) {
                String line = it.next();
                if (line.isBlank()) continue;
                String[] cols = line.split(",");
                if (cols.length >= 4) {
                    try {
                        String sku = cols[0].trim();
                        String name = cols[1].trim();
                        String desc = cols[2].trim();
                        double price = Double.parseDouble(cols[3].trim());
                        int stock = (cols.length > 4) ? Integer.parseInt(cols[4].trim()) : 0;

                        if (productService.findBySku(sku).isEmpty()) {
                            Product p = new Product(sku, name, desc, BigDecimal.valueOf(price));
                            Product saved = productService.createProduct(p);
                            inventoryService.createInventory(saved, stock);
                            count++;
                        } else {
                            skipped++;
                        }
                    } catch (Exception e) {
                        skipped++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fel vid läsning: " + e.getMessage());
        }
        System.out.printf("Klart! %d produkter importerade, %d skippade.%n", count, skipped);
    }

    @Transactional
    public void importCustomers(Path filePath) {
        System.out.println("Importerar kunder...");
        int count = 0;
        int skipped = 0;

        try (Stream<String> lines = Files.lines(filePath)) {
            Iterator<String> it = lines.iterator();
            if (it.hasNext()) it.next();

            while (it.hasNext()) {
                String line = it.next();
                if (line.isBlank()) continue;
                String[] cols = line.split(",");
                if (cols.length >= 2) {
                    try {
                        String email = cols[0].trim();
                        String name = cols[1].trim();
                        if (customerService.findByEmail(email).isEmpty()) {
                            customerService.createCustomer(email, name);
                            count++;
                        } else {
                            skipped++;
                        }
                    } catch (Exception e) {
                        skipped++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fel vid läsning: " + e.getMessage());
        }
        System.out.printf("Klart! %d kunder importerade, %d skippade.%n", count, skipped);
    }

    @Transactional
    public void importInventory(Path filePath) {
        System.out.println("Uppdaterar lager...");
        int count = 0;
        try (Stream<String> lines = Files.lines(filePath)) {
            Iterator<String> it = lines.iterator();
            if (it.hasNext()) it.next();

            while (it.hasNext()) {
                String line = it.next();
                if (line.isBlank()) continue;
                String[] cols = line.split(",");
                if (cols.length >= 2) {
                    String sku = cols[0].trim();
                    int stock = Integer.parseInt(cols[1].trim());
                    productService.findBySku(sku).ifPresent(p -> {
                        inventoryService.addStock(p, stock);
                    });
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println("Fel: " + e.getMessage());
        }
        System.out.println("Lager uppdaterat för " + count + " rader.");
    }

    @Transactional
    public void importOrders(Path filePath) {
        System.out.println("Importerar ordrar...");
        int count = 0;
        int skipped = 0;

        try (Stream<String> lines = Files.lines(filePath)) {
            Iterator<String> it = lines.iterator();
            if (it.hasNext()) it.next();

            while (it.hasNext()) {
                String line = it.next();
                if (line.isBlank()) continue;

                String[] cols = line.split(",");
                if (cols.length >= 4) {
                    try {
                        String email = cols[0].trim();
                        String statusStr = cols[1].trim();
                        String dateStr = cols[2].trim();
                        String itemsStr = cols[3].trim();

                        Optional<Customer> customerOpt = customerService.findByEmail(email);
                        if (customerOpt.isEmpty()) {
                            logger.warn("Okänd kund: " + email);
                            skipped++;
                            continue;
                        }

                        Order order = new Order();
                        order.setCustomer(customerOpt.get());
                        order.setStatus(OrderStatus.valueOf(statusStr.toUpperCase()));
                        order.setCreatedAt(LocalDateTime.parse(dateStr));

                        BigDecimal total = BigDecimal.ZERO;
                        List<OrderItem> orderItems = new ArrayList<>();

                        String[] itemsData = itemsStr.split("\\|");
                        for (String itemData : itemsData) {
                            String[] parts = itemData.split(":"); // SKU:QTY
                            String sku = parts[0].trim();
                            int qty = Integer.parseInt(parts[1].trim());

                            Optional<Product> productOpt = productService.findBySku(sku);
                            if (productOpt.isPresent()) {
                                Product p = productOpt.get();
                                OrderItem oi = new OrderItem();
                                oi.setProduct(p);
                                oi.setQty(qty);
                                oi.setUnitPrice(p.getPrice());
                                BigDecimal lineTotal = p.getPrice().multiply(BigDecimal.valueOf(qty));
                                oi.setLineTotal(lineTotal);

                                orderItems.add(oi);
                                total = total.add(lineTotal);
                            }
                        }

                        if (!orderItems.isEmpty()) {
                            order.setTotal(total);
                            order.getItems().addAll(orderItems);

                            orderService.saveImportedOrder(order);
                            count++;
                        } else {
                            skipped++;
                        }

                    } catch (Exception e) {
                        logger.error("Fel på orderrad: " + line, e);
                        skipped++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fel vid läsning: " + e.getMessage());
        }
        System.out.printf("Klart! %d ordrar importerade, %d skippade.%n", count, skipped);
    }
}