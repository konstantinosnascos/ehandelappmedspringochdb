package com.example.ecommerce.service;

import com.example.ecommerce.model.Inventory;
import com.example.ecommerce.repository.InventoryRepository;
import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Optional<Inventory> getStock(Long productId) {
        return inventoryRepository.findById(productId);
    }

    public int getStockLevel(Long productId) {
        return inventoryRepository.findById(productId)
                .map(Inventory::getInStock)
                .orElse(0);
    }

    public boolean hasStock(Long productId, int quantity) {
        return inventoryRepository.findById(productId)
                .map(inv -> inv.hasStock(quantity))
                .orElse(false);
    }

    public List<Inventory> getLowStock(int minStock) {
        return inventoryRepository.findByInStockLessThan(minStock);
    }

    @Transactional
    public void decrease(Long productId, int amount) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produkt " + productId + " finns inte"));

        if (!inventory.hasStock(amount)) {
            throw new InsufficientStockException("Otillräckligt lager för produkt " + productId);
        }

        inventory.decreaseStock(amount);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void increase(Long productId, int amount) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produkt " + productId + " finns inte"));
        inventory.increaseStock(amount);
        inventoryRepository.save(inventory);
    }
}
//Lagerhantering

//Hämta lagerpost för produkt
//Hämta antal i lager
//Kolla om tillräckligt lager finns
//Hämta produkter med lager under minStock
//Minska lager
//Öka lager