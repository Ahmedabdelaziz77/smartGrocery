package com.smartgroceryApp.smartgrocery.service;

import com.smartgroceryApp.smartgrocery.common.exception.BusinessException;
import com.smartgroceryApp.smartgrocery.common.exception.ResourceNotFoundException;
import com.smartgroceryApp.smartgrocery.common.util.CurrentUserService;
import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import com.smartgroceryApp.smartgrocery.product.repository.ApprovedProductRepository;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.AddShoppingListItemRequestDto;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.ShoppingListItemResponse;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.ShoppingListResponse;
import com.smartgroceryApp.smartgrocery.shoppinglist.dto.UpdateShoppingListItemRequestDto;
import com.smartgroceryApp.smartgrocery.shoppinglist.entity.ShoppingList;
import com.smartgroceryApp.smartgrocery.shoppinglist.entity.ShoppingListItem;
import com.smartgroceryApp.smartgrocery.shoppinglist.mapper.ShoppingListMapper;
import com.smartgroceryApp.smartgrocery.shoppinglist.repository.ShoppingListItemRepository;
import com.smartgroceryApp.smartgrocery.shoppinglist.repository.ShoppingListRepository;
import com.smartgroceryApp.smartgrocery.shoppinglist.service.ShoppingListService;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingListServiceTest {

    @Mock private ShoppingListRepository shoppingListRepository;
    @Mock private ShoppingListItemRepository shoppingListItemRepository;
    @Mock private ApprovedProductRepository approvedProductRepository;
    @Mock private CurrentUserService currentUserService;
    @Mock private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private ShoppingListService shoppingListService;

    private User testUser;
    private ShoppingList testShoppingList;
    private ApprovedProduct testProduct;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFullName("User Ahmed");
        testUser.setEmail("zozahmed770@gmail.com");

        testShoppingList = new ShoppingList();
        testShoppingList.setId(1L);
        testShoppingList.setUser(testUser);
        testShoppingList.setName("Ahmed's Shopping List");

        testProduct = new ApprovedProduct();
        testProduct.setId(10L);
        testProduct.setName("Product");
        testProduct.setEstimatedPrice(BigDecimal.valueOf(4.99));
        testProduct.setActive(true);
    }

    @Test
    @DisplayName("getCurrentUserShoppingList should create list if none exists")
    void getCurrentUserShoppingList_noExistingList_shouldCreateNew() {
        ShoppingListResponse expectedResponse = new ShoppingListResponse(
                1L, "Ahmed's Shopping List", Collections.emptyList(), BigDecimal.ZERO
        );

        when(currentUserService.getCurrentUser()).thenReturn(testUser);
        when(shoppingListRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(shoppingListRepository.save(any(ShoppingList.class))).thenReturn(testShoppingList);
        when(shoppingListItemRepository.findByShoppingList(testShoppingList)).thenReturn(Collections.emptyList());
        when(shoppingListMapper.toResponse(testShoppingList, Collections.emptyList())).thenReturn(expectedResponse);

        ShoppingListResponse res = shoppingListService.getCurrentUserShoppingList();

        assertThat(res.getName()).isEqualTo("Ahmed's Shopping List");
        assertThat(res.getItems()).isEmpty();
        verify(shoppingListRepository).save(any(ShoppingList.class));
    }

    @Test
    @DisplayName("addItem should add product to shopping list")
    void addItem_validProduct_shouldAddSuccessfully() {
        AddShoppingListItemRequestDto request = new AddShoppingListItemRequestDto(10L, 2);

        ShoppingListItem savedItem = new ShoppingListItem();
        savedItem.setId(100L);
        savedItem.setShoppingList(testShoppingList);
        savedItem.setProduct(testProduct);
        savedItem.setQuantity(2);

        ShoppingListItemResponse itemRes = new ShoppingListItemResponse();

        itemRes.id = 100L;
        itemRes.productId = 10L;
        itemRes.productName = "Product";
        itemRes.brand = "Brand";
        itemRes.category = "Category";
        itemRes.imageUrl = null;
        itemRes.estimatedPrice = BigDecimal.valueOf(4.99);
        itemRes.quantity = 2;
        itemRes.totalPrice = BigDecimal.valueOf(9.98);


        ShoppingListResponse expectedResponse = new ShoppingListResponse(
                1L, "Ahmed's Shopping List", List.of(itemRes), BigDecimal.valueOf(9.98)
        );

        when(currentUserService.getCurrentUser()).thenReturn(testUser);
        when(shoppingListRepository.findByUser(testUser)).thenReturn(Optional.of(testShoppingList));
        when(approvedProductRepository.findByIdAndActiveTrue(10L)).thenReturn(Optional.of(testProduct));
        when(shoppingListItemRepository.findByShoppingListAndProduct(testShoppingList, testProduct))
                .thenReturn(Optional.empty());
        when(shoppingListItemRepository.findByShoppingList(testShoppingList)).thenReturn(List.of(savedItem));
        when(shoppingListMapper.toResponse(eq(testShoppingList), anyList())).thenReturn(expectedResponse);

        ShoppingListResponse res = shoppingListService.addItem(request);

        assertThat(res.getItems()).hasSize(1);
        assertThat(res.getTotalEstimatedPrice()).isEqualTo(BigDecimal.valueOf(9.98));
        verify(shoppingListItemRepository).save(any(ShoppingListItem.class));
    }

    @Test
    @DisplayName("addItem should throw if product already in list")
    void addItem_duplicateProduct_shouldThrow() {
        AddShoppingListItemRequestDto req = new AddShoppingListItemRequestDto(10L, 1);

        ShoppingListItem existing = new ShoppingListItem();
        existing.setProduct(testProduct);

        when(currentUserService.getCurrentUser()).thenReturn(testUser);
        when(shoppingListRepository.findByUser(testUser)).thenReturn(Optional.of(testShoppingList));
        when(approvedProductRepository.findByIdAndActiveTrue(10L)).thenReturn(Optional.of(testProduct));
        when(shoppingListItemRepository.findByShoppingListAndProduct(testShoppingList, testProduct))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> shoppingListService.addItem(req))
                .isInstanceOf(BusinessException.class)
                .hasMessage("product already exists in shopping list");
    }

    @Test
    @DisplayName("addItem should throw if product not found")
    void addItem_nonExistentProduct_shouldThrow() {
        AddShoppingListItemRequestDto req = new AddShoppingListItemRequestDto(999L, 1);

        when(currentUserService.getCurrentUser()).thenReturn(testUser);
        when(shoppingListRepository.findByUser(testUser)).thenReturn(Optional.of(testShoppingList));
        when(approvedProductRepository.findByIdAndActiveTrue(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shoppingListService.addItem(req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("updateItem should update quantity")
    void updateItem_validItem_shouldUpdateQuantity() {
        UpdateShoppingListItemRequestDto req = new UpdateShoppingListItemRequestDto(5);

        ShoppingListItem existing = new ShoppingListItem();


        existing.setId(100L);
        existing.setShoppingList(testShoppingList);
        existing.setProduct(testProduct);
        existing.setQuantity(2);

        when(currentUserService.getCurrentUser()).thenReturn(testUser);
        when(shoppingListRepository.findByUser(testUser)).thenReturn(Optional.of(testShoppingList));
        when(shoppingListItemRepository.findByIdAndShoppingList(100L, testShoppingList))
                .thenReturn(Optional.of(existing));
        when(shoppingListItemRepository.findByShoppingList(testShoppingList)).thenReturn(List.of(existing));
        when(shoppingListMapper.toResponse(eq(testShoppingList), anyList())).thenReturn(
                new ShoppingListResponse(1L, "list", Collections.emptyList(), BigDecimal.ZERO)
        );

        shoppingListService.updateItem(100L, req);

        assertThat(existing.getQuantity()).isEqualTo(5);
        verify(shoppingListItemRepository).save(existing);
    }

    @Test
    @DisplayName("deleteItem should remove item from list")
    void deleteItem_validItem_shouldDelete() {
        ShoppingListItem existing = new ShoppingListItem();
        existing.setId(100L);

        when(currentUserService.getCurrentUser()).thenReturn(testUser);
        when(shoppingListRepository.findByUser(testUser)).thenReturn(Optional.of(testShoppingList));
        when(shoppingListItemRepository.findByIdAndShoppingList(100L, testShoppingList))
                .thenReturn(Optional.of(existing));

        shoppingListService.deleteItem(100L);

        verify(shoppingListItemRepository).delete(existing);
    }

    @Test
    @DisplayName("clearAll should delete all items from shopping list")
    void clearAll_shouldDeleteAllItems() {
        when(currentUserService.getCurrentUser()).thenReturn(testUser);
        when(shoppingListRepository.findByUser(testUser)).thenReturn(Optional.of(testShoppingList));

        shoppingListService.clearAll();

        verify(shoppingListItemRepository).deleteByShoppingList(testShoppingList);
    }

    @Test
    @DisplayName("deleteItem should throw if item not found")
    void deleteItem_nonExistentItem_shouldThrow() {
        when(currentUserService.getCurrentUser()).thenReturn(testUser);
        when(shoppingListRepository.findByUser(testUser)).thenReturn(Optional.of(testShoppingList));
        when(shoppingListItemRepository.findByIdAndShoppingList(999L, testShoppingList))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> shoppingListService.deleteItem(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
