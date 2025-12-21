package com.example.ecommerce.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.*;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CartItem> items = new ArrayList<>();

    public Cart() {} // JPA kräver denna

    public Cart(Customer customer) {
        this.customer = customer;
    }

    // getters
    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public List<CartItem> getItems() { return items; }
}
