package com.smartgroceryApp.smartgrocery.shoppinglist.entity;


import com.smartgroceryApp.smartgrocery.common.entity.ParentEntity;
import com.smartgroceryApp.smartgrocery.product.entity.ApprovedProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity
@Table(
        name = "shopping_list_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "shopping_list_items_unqiue_constraints",
                        columnNames = {"shopping_list_id", "product_id"}
                )
        }
)
public class ShoppingListItem extends ParentEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shopping_list_id", nullable = false)
    private ShoppingList shoppingList;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ApprovedProduct product;

    @Column(nullable = false)
    private Integer quantity;
}
