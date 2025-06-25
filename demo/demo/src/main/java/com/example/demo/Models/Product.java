package com.example.demo.Models;


import com.example.demo.Models.Enums.PhoneModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "products")
@JsonIgnoreProperties({"category","orders"})

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Product name cannot be null.")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Product name cannot be null.")
    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_model")
    private PhoneModel phoneModel;


    private boolean isDeleted = false;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private Set<Order> orders = new java.util.HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category = new Category();

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<Review> reviews = new java.util.HashSet<>();
}
