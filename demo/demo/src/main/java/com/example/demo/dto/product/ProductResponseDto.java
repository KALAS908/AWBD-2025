package com.example.demo.dto.product;

import com.example.demo.Models.Category;
import com.example.demo.Models.Enums.PhoneModel;
import com.example.demo.dto.review.ReviewResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private String name;
    private Double price;
    private PhoneModel phoneModel;
    private Category category;
    private Set<ReviewResponseDto> reviews;
}