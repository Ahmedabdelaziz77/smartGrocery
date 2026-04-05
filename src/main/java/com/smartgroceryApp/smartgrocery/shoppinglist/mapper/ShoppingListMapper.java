package com.smartgroceryApp.smartgrocery.shoppinglist.mapper;

import com.smartgroceryApp.smartgrocery.shoppinglist.dto.ShoppingListItemResponse;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.ShoppingListResponse;
import com.smartgroceryApp.smartgrocery.shoppinglist.entity.ShoppingList;
import com.smartgroceryApp.smartgrocery.shoppinglist.entity.ShoppingListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingListMapper {
    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalEstimatedPrice", source = "items", qualifiedByName = "calculateTotal")
    ShoppingListResponse toResponse(ShoppingList shoppingList, List<ShoppingListItem> items);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "brand", source = "product.brand")
    @Mapping(target = "category", source = "product.category")
    @Mapping(target = "imageUrl", source = "product.imageUrl")
    @Mapping(target = "estimatedPrice", source = "product.estimatedPrice")
    @Mapping(target = "totalPrice", expression = "java(item.getProduct().getEstimatedPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))")
    ShoppingListItemResponse toItemResponse(ShoppingListItem item);

    @Named("calculateTotal")
    default BigDecimal calculateTotal(List<ShoppingListItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(item -> item.getProduct().getEstimatedPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
