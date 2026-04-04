package com.smartgroceryApp.smartgrocery.product.entity;

import com.smartgroceryApp.smartgrocery.common.entity.ParentEntity;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(
        name = "approved_products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "approved_products_external_unique_constraints",
                        columnNames = {"external_id", "external_source"}
                )
        }
)
public class ApprovedProduct extends ParentEntity {
    @Column(name = "external_id", nullable = false)
    private String externalId;



    @Column(name = "external_source", nullable = false)
    private String externalSource = "OPEN_FOOD_FACTS";

    @Column(nullable = false)
    private String name;

    @Column
    private String brand;


    @Column
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column
    private Double calories;

    @Column
    private Double protein;

    @Column
    private Double carbs;

    @Column
    private Double fat;

    @Column(name = "estimated_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approved_by", nullable = false)
    private User approvedBy;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
