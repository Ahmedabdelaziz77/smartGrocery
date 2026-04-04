package com.smartgroceryApp.smartgrocery.openfoodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @AllArgsConstructor @NoArgsConstructor
public class ProductDetailsResponseDto {
    private Integer status;
    private ProductDto product;
}
