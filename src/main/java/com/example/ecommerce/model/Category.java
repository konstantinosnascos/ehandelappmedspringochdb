package com.example.ecommerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    //Tom konstruktor för jpa
    protected Category() {}

    //Konstruktor för använding i andra Service klasser
    public Category(String name)
    {
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

}
