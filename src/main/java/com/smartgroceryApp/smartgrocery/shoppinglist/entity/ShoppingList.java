package com.smartgroceryApp.smartgrocery.shoppinglist.entity;

import com.smartgroceryApp.smartgrocery.common.entity.ParentEntity;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shopping_lists")
public class ShoppingList extends ParentEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    @Column(nullable = false)
    private String name;
}
