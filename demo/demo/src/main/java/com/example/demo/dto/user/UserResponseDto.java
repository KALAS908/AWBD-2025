package com.example.demo.dto.user;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String userName;
    private int age;
    private String email;
    private String password;
    private String login;
    private String token;
    private String message;
    private boolean isAdmin;
}