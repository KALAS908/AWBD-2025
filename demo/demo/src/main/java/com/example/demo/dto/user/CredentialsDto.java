package com.example.demo.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


public class CredentialsDto implements Serializable {
  @Setter
  @Getter
  public String email;

  @Setter
  @Getter
  public String password;


    public CredentialsDto() {
    }
    public CredentialsDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
