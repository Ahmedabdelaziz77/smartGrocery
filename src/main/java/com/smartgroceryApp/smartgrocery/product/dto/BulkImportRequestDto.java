package com.smartgroceryApp.smartgrocery.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class BulkImportRequestDto {
    @NotEmpty(message = "products mustn't be empty")
    List<@Valid ImportProductRequestDto> products;
}
