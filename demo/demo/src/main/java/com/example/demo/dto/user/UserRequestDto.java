package com.example.demo.dto.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto implements Serializable {

    private UUID id;
    private String firstName;
    private String lastName;
    private String userName;
    private int age;
    private String email;
    private String password;

    public UserRequestDto(String firstName, String lastName, String userName, int age, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.age = age;
        this.email = email;
        this.password = password;
    }


}
