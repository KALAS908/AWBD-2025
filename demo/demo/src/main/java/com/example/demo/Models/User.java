package com.example.demo.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
@JsonIgnoreProperties("order")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @NotNull(message = "First name cannot be null.")
    @Column(name = "first_name")
    private String firstName;

    @Setter
    @NotNull(message = "Last name cannot be null.")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "User name cannot be null.")
    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "age")
    private int age;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Ticket> tickets;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;

    private boolean isDeleted = false;


}
