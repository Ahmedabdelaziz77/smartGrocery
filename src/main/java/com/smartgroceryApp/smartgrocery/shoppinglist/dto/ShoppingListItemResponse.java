package com.smartgroceryApp.smartgrocery.shoppinglist.dto;

import java.math.BigDecimal;

public class ShoppingListItemResponse {
    public Long id;
    public Long productId;
    public String productName;
    public String brand;
    public String category;
    public String imageUrl;
    public BigDecimal estimatedPrice;
    public Integer quantity;
    public BigDecimal totalPrice;
}
