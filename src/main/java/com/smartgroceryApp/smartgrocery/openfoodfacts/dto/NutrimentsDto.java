package com.smartgroceryApp.smartgrocery.openfoodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @AllArgsConstructor @NoArgsConstructor
public class NutrimentsDto {
    @JsonProperty("energy-kcal_100g")
    private Double energyKcal100g;


    @JsonProperty("proteins_100g")
    private Double proteins100g;

    @JsonProperty("carbohydrates_100g")
    private Double carbohydrates100g;

    @JsonProperty("fat_100g")
    private Double fat100g;
}
