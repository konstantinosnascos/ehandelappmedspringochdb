package com.example.ecommerce.runner;

import com.example.ecommerce.menu.MenuHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class MenuRunner implements CommandLineRunner {
    private final MenuHandler menuHandler;

    public MenuRunner(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }

    @Override
    public void run(String... args) {
        menuHandler.runMainMenu();
    }
}