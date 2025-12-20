package com.example.ecommerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "in_stock", nullable = false)
    private Integer inStock = 0;

    public Inventory() {}

    public Inventory(Product product, Integer inStock) {
        this.product = product;
        this.inStock = inStock;
    }

    // Getters & Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public boolean hasStock(int amount) {
        return this.inStock >= amount;
    }

    public void decreaseStock(int amount) {
        if (!hasStock(amount)) {
            throw new IllegalArgumentException(
                    "Otillräckligt lager. Tillgängligt: " + inStock + ", Begärt: " + amount
            );
        }
        this.inStock -= amount;
    }

    public void increaseStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Kan inte öka med negativt värde");
        }
        this.inStock += amount;
    }

    @Override
    public String toString() {
        String productName = (product != null) ? product.getName() : "Okänd";
        return String.format("%s: %d i lager", productName, inStock);
    }
}