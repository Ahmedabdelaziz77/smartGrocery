package com.smartgroceryApp.smartgrocery.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ImportProductRequestDto {

    @NotBlank(message = "external product idd is required!")
    String externalId;

    @NotNull(message = "estimated price is required!!")
    @DecimalMin(value = "0.01", message = "estimated price must be greater than 0")
    BigDecimal estimatedPrice;
}
