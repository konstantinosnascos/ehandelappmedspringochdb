package com.example.ecommerce.runner;

import com.example.ecommerce.menu.MainMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
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
