package com.example.demo.dto.user;


import lombok.AllArgsConstructor;

public record SignUpDto(
    int age,
    String firstName,
    String lastName,
    String userName,
    String email,
    String password
    ){}

