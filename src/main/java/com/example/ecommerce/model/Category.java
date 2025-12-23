package com.example.ecommerce.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    //Tom konstruktor för jpa
    protected Category() {}

    //Konstruktor för använding i andra Service klasser
    public Category(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }
}




//@Entity
//@Table(name = "categories")
//public class Category {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @Column(name = "name", nullable = false, unique = true, length = 100)
//    private String name;
//
//    //Tom konstruktor för jpa
//    protected Category() {}
//
//    //Konstruktor för använding i andra Service klasser
//    public Category(String name)
//    {
//        this.name = name;
//    }
//
//
//    public long getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//}
