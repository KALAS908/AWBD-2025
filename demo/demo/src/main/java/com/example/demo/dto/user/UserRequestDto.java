package com.example.demo.dto.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String userName;
    private int age;
    private String email;
    private String password;
}
