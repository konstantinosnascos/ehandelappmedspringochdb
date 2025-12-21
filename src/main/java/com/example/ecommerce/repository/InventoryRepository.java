package com.example.ecommerce.repository;

import com.example.ecommerce.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    List<Inventory> findByInStockLessThan(Integer minStock);

}