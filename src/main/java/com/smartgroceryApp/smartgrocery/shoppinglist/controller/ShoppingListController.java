package com.smartgroceryApp.smartgrocery.shoppinglist.controller;

import com.smartgroceryApp.smartgrocery.shoppinglist.dto.AddShoppingListItemRequestDto;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.ShoppingListResponse;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.UpdateShoppingListItemRequestDto;
import com.smartgroceryApp.smartgrocery.shoppinglist.service.ShoppingListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shopping-list")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @GetMapping
    public ResponseEntity<ShoppingListResponse> getShoppingList() {

        return ResponseEntity.ok(shoppingListService.getCurrentUserShoppingList());
    }

    @PostMapping("/items")
    public ResponseEntity<ShoppingListResponse> addItem(
            @Valid @RequestBody AddShoppingListItemRequestDto req
    ) {
        return ResponseEntity.ok(shoppingListService.addItem(req));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ShoppingListResponse> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShoppingListItemRequestDto req
    ) {
        return ResponseEntity.ok(shoppingListService.updateItem(id, req));

    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        shoppingListService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> clearAll() {
        shoppingListService.clearAll();
        return ResponseEntity.noContent().build();
    }
}
