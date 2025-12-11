package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

//    @Id: Detta är primärnyckeln
//    @GeneratedValue: Databasen genererar ID automatiskt (auto-increment)
//    IDENTITY: Använder databas-specifik auto-increment (t.ex. PostgreSQL SERIAL)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", unique = true, nullable = false, length = 50)
    private String sku;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Product() {} //krävs no-arg konstruktor

    public Product(String sku, String name, String description, double price) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = true;
    }


//    Alternativ:
//    @PreUpdate - Före uppdatering
//    @PostPersist - Efter sparning
//    @PostLoad - Efter laddning från DB
//

    // Lifecycle callback - körs automatiskt innan entitet sparas första gången
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (active == false) { // om inte satt
            active = true;
        }
    }

    // Getters & Setters (SAMMA som innan, ingen ändring)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %.2f kr%s",
                sku, name, price, active ? "" : " (INAKTIV)");
    }
}


//SQL som genereras:
//När du startar applikationen skapar Hibernate denna tabell:
//
//sql
//CREATE TABLE products (
//        id BIGSERIAL PRIMARY KEY,
//        sku VARCHAR(50) NOT NULL UNIQUE,
//name VARCHAR(200) NOT NULL,
//description VARCHAR(1000),
//price DOUBLE PRECISION NOT NULL,
//active BOOLEAN NOT NULL DEFAULT true,
//created_at TIMESTAMP NOT NULL
//);