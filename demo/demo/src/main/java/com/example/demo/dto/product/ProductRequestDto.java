package com.example.demo.dto.product;

import com.example.demo.Models.Enums.PhoneModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String name;
    private Double price;
    private PhoneModel phoneModel;
    private UUID categoryId;
}
