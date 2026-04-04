package com.smartgroceryApp.smartgrocery.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class SignupUserRequest {
    @NotBlank(message = "full name is required!")
    public String fullName;


    @NotBlank(message = "email is required!")
    @Email(message = "email is invalid")
    @Size(max = 255, message = "email mustn't exceed 255 chars!")
    public String email;


    @NotBlank(message = "password is required !")
    @Size(min = 6, max = 50, message = "password must be between 6 and 50 chars!")
    public String password;
}
