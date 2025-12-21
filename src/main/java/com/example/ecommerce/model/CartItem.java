package com.example.ecommerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    private Cart cart;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    private int qty;
}
