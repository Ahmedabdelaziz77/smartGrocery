package com.smartgroceryApp.smartgrocery.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ProductDetailsResponseDto {
    public Long id;


    public String name;
    public String brand;
    public String category;
    public String description;
    public String imageUrl;
    public Double calories;
    public Double protein;
    public Double carbs;
    public Double fat;
    public BigDecimal estimatedPrice;
}
