package com.smartgroceryApp.smartgrocery.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ProductSearchRequestDto {
    public String name;

    public String category;
}
