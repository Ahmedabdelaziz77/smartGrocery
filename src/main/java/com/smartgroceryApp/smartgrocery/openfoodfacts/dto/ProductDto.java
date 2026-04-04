package com.smartgroceryApp.smartgrocery.openfoodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @AllArgsConstructor @NoArgsConstructor
public class ProductDto {
    private String code;
    @JsonProperty("product_name")
    private String productName;

    private String brands;

    private String categories;

    @JsonProperty("generic_name")
    private String genericName;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("nutriments")
    private NutrimentsDto nutriments;
}
