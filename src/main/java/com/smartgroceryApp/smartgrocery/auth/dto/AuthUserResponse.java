package com.smartgroceryApp.smartgrocery.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AuthUserResponse {
    private Long userId;

    private String fullName;

    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;
}
