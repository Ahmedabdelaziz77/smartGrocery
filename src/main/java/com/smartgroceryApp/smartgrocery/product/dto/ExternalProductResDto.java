package com.smartgroceryApp.smartgrocery.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ExternalProductResDto {
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
}
