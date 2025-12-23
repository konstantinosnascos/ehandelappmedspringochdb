package com.example.ecommerce.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Component
public class InputHelper {
    private final Scanner scanner;
    private static final Logger logger = LoggerFactory.getLogger(InputHelper.class);

    public InputHelper() {
        this.scanner = new Scanner(System.in);
    }

    public int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                if (scanner.hasNextInt()) {
                    int value = scanner.nextInt();
                    scanner.nextLine();
                    return value;
                } else {
                    System.out.println("Felaktig inmatning. Ange ett heltal.");
                    logger.warn("Ogiltig int-inmatning");
                    scanner.next();
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                logger.error("Fel vid inläsning av heltal", e);
                return -1;
            }
        }
    }

    public double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                if (scanner.hasNextDouble()) {
                    double value = scanner.nextDouble();
                    scanner.nextLine();
                    return value;
                } else {
                    System.out.println("Felaktig inmatning. Ange ett nummer.");
                    logger.warn("Ogiltig double-inmatning");
                    scanner.next();
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                logger.error("Fel vid inläsning av decimaltal", e);
                return 0.0;
            }
        }
    }

    public String getString(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    return input;
                }
                System.out.println("Fältet får inte vara tomt.");
                logger.warn("Tom sträng inmatad");
            } catch (NoSuchElementException | IllegalStateException e) {
                logger.error("Fel vid inläsning av sträng", e);
                return "";
            }
        }
    }

    public String getOptionalString(String prompt) {
        try {
            System.out.print(prompt);
            return scanner.nextLine().trim();
        } catch (NoSuchElementException | IllegalStateException e) {
            logger.error("Fel vid inläsning av valfri sträng", e);
            return "";
        }
    }
}