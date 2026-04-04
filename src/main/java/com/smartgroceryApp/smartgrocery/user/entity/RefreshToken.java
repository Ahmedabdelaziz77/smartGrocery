package com.smartgroceryApp.smartgrocery.user.entity;

import com.smartgroceryApp.smartgrocery.common.entity.ParentEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends ParentEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked")
    private boolean isRevoked = false;
}
