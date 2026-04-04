package com.smartgroceryApp.smartgrocery.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ApprovedProductResponseDto {
    public Long id;
    public String externalId;
    public String externalSource;

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
    public boolean active;
    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;
}
