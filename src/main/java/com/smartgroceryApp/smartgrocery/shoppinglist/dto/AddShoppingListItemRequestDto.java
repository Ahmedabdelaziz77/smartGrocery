package com.smartgroceryApp.smartgrocery.shoppinglist.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AddShoppingListItemRequestDto {
    @NotNull(message = "product id is required")
    Long productId;


    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be greater than 0!!!")
    Integer quantity;
}
