package com.smartgroceryApp.smartgrocery.common.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginUserRequest {
    @NotBlank(message = "email is required")
    @Email(message = "email format is invalid")
    @Size(max = 255, message = "email mustn't exceed 255 chars")
    public String email;

    @NotBlank(message = "password is required!")
    @Size(min = 6, max = 50, message = "password must be between 6 and 50 chars!")
    public String password;
}
