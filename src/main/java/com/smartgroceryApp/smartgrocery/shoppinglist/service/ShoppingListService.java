package com.smartgroceryApp.smartgrocery.shoppinglist.service;

import com.smartgroceryApp.smartgrocery.common.exception.BusinessException;
import com.smartgroceryApp.smartgrocery.common.exception.ResourceNotFoundException;
import com.smartgroceryApp.smartgrocery.common.util.CurrentUserService;
import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import com.smartgroceryApp.smartgrocery.product.repository.ApprovedProductRepository;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.AddShoppingListItemRequestDto;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.ShoppingListResponse;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.UpdateShoppingListItemRequestDto;
import com.smartgroceryApp.smartgrocery.shoppinglist.entity.ShoppingList;
import com.smartgroceryApp.smartgrocery.shoppinglist.entity.ShoppingListItem;
import com.smartgroceryApp.smartgrocery.shoppinglist.mapper.ShoppingListMapper;
import com.smartgroceryApp.smartgrocery.shoppinglist.repository.ShoppingListItemRepository;
import com.smartgroceryApp.smartgrocery.shoppinglist.repository.ShoppingListRepository;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingListService {
    private final ShoppingListRepository shoppingListRepository;

    private final ShoppingListItemRepository shoppingListItemRepository;

    private final ApprovedProductRepository approvedProductRepository;

    private final CurrentUserService currentUserService;

    private final ShoppingListMapper shoppingListMapper;



    @Transactional(readOnly = true)
    public ShoppingListResponse getCurrentUserShoppingList() {
        ShoppingList shoppingList = getOrCreateShoppingList(currentUserService.getCurrentUser());
        List<ShoppingListItem> items = shoppingListItemRepository.findByShoppingList(shoppingList);
        return shoppingListMapper.toResponse(shoppingList, items);
    }


    @Transactional
    public ShoppingListResponse addItem(AddShoppingListItemRequestDto req) {
        User user = currentUserService.getCurrentUser();
        ShoppingList shoppingList = getOrCreateShoppingList(user);

        ApprovedProduct product = approvedProductRepository.findByIdAndActiveTrue(req.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Approved product not found with id -> " + req.getProductId()));

        shoppingListItemRepository.findByShoppingListAndProduct(shoppingList, product)
                .ifPresent(existingItem -> {
                    throw new BusinessException("product already exists in shopping list");
                });

        ShoppingListItem item = new ShoppingListItem();
        item.setShoppingList(shoppingList);
        item.setProduct(product);
        item.setQuantity(req.getQuantity());

        shoppingListItemRepository.save(item);
        log.info("added product id={}, quantity={} to shopping list id={} for user={}",
                product.getId(), req.getQuantity(), shoppingList.getId(), user.getEmail());
        List<ShoppingListItem> items = shoppingListItemRepository.findByShoppingList(shoppingList);
        return shoppingListMapper.toResponse(shoppingList, items);
    }


    @Transactional
    public ShoppingListResponse updateItem(Long itemId, UpdateShoppingListItemRequestDto req) {

        User user = currentUserService.getCurrentUser();

        ShoppingList shoppingList = getOrCreateShoppingList(user);

        ShoppingListItem item = shoppingListItemRepository.findByIdAndShoppingList(itemId, shoppingList)
                .orElseThrow(() -> new ResourceNotFoundException("shopping list item doesn't found with id -> " + itemId));

        item.setQuantity(req.getQuantity());
        shoppingListItemRepository.save(item);
        log.info("updated shopping list item id={} to qty={} with user={}", itemId, req.getQuantity(), user.getEmail());

        List<ShoppingListItem> items = shoppingListItemRepository.findByShoppingList(shoppingList);
        return shoppingListMapper.toResponse(shoppingList, items);
    }


    public void deleteItem(Long itemId) {
        User user = currentUserService.getCurrentUser();
        ShoppingList shoppingList = getOrCreateShoppingList(user);

        ShoppingListItem item = shoppingListItemRepository.findByIdAndShoppingList(itemId, shoppingList)
                .orElseThrow(() -> new ResourceNotFoundException("shopping list item not found with id -> " + itemId));

        shoppingListItemRepository.delete(item);
        log.info("removed shopping list item id={} !", itemId);
    }

    @Transactional
    public void clearAll() {

        User user = currentUserService.getCurrentUser();

        ShoppingList shoppingList = getOrCreateShoppingList(user);
        shoppingListItemRepository.deleteByShoppingList(shoppingList);
        log.info("cleared all items from shopping list id={} for user={}", shoppingList.getId(), user.getEmail());
    }

    private ShoppingList getOrCreateShoppingList(User user) {
        return
                shoppingListRepository.findByUser(user)
                .orElseGet(() -> {
                    ShoppingList shoppingList = new ShoppingList();
                    shoppingList.setUser(user);
                    shoppingList.setName(user.getFullName() + "'s Shopping List");
                    return shoppingListRepository.save(shoppingList);
                });
    }
}
