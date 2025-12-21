package com.example.ecommerce.runner;

import com.example.ecommerce.menu.MainMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MenuRunner implements CommandLineRunner {
    private final MainMenu mainMenu;

    public MenuRunner(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public void run(String... args) throws Exception {
        mainMenu.show();
    }
}
