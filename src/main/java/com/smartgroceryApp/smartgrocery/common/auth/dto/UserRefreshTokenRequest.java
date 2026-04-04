package com.smartgroceryApp.smartgrocery.common.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRefreshTokenRequest {
    @NotBlank(message = "refresh token is required!")
    public String refreshToken;
}
