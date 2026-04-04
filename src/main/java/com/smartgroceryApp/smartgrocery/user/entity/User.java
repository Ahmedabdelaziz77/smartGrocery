package com.smartgroceryApp.smartgrocery.user.entity;

import com.smartgroceryApp.smartgrocery.common.entity.ParentEntity;
import com.smartgroceryApp.smartgrocery.common.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "users")
public class User extends ParentEntity {
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private boolean enabled = true;
}
