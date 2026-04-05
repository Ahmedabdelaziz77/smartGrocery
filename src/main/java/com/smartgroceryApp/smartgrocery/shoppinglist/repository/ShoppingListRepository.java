package com.smartgroceryApp.smartgrocery.shoppinglist.repository;

import com.smartgroceryApp.smartgrocery.shoppinglist.entity.ShoppingList;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    Optional<ShoppingList> findByUser(User user);
}
