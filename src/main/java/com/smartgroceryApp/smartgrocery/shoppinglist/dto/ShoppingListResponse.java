package com.smartgroceryApp.smartgrocery.shoppinglist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListResponse {
    public Long id;
    public String name;
    public List<ShoppingListItemResponse> items;
    public BigDecimal totalEstimatedPrice;
}
