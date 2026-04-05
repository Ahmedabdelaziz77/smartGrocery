package com.smartgroceryApp.smartgrocery.shoppinglist.repository;

import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import com.smartgroceryApp.smartgrocery.shoppinglist.entity.ShoppingList;
import com.smartgroceryApp.smartgrocery.shoppinglist.entity.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    List<ShoppingListItem> findByShoppingList(ShoppingList shoppingList);

    Optional<ShoppingListItem> findByShoppingListAndProduct(ShoppingList shoppingList, ApprovedProduct product);

    Optional<ShoppingListItem> findByIdAndShoppingList(Long id, ShoppingList shoppingList);

    void deleteByShoppingList(ShoppingList shoppingList);
}
