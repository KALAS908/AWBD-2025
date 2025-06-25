package com.example.demo.dto.product;

import com.example.demo.Models.Enums.PhoneModel;
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
public class ProductRequestDto implements Serializable {
    private String name;
    private Double price;
    private PhoneModel phoneModel;
    private UUID categoryId;
}
