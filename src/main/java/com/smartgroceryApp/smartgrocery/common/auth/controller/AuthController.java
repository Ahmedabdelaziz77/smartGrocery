package com.smartgroceryApp.smartgrocery.common.auth.controller;

import com.smartgroceryApp.smartgrocery.common.auth.dto.AuthUserResponse;
import com.smartgroceryApp.smartgrocery.common.auth.dto.LoginUserRequest;
import com.smartgroceryApp.smartgrocery.common.auth.dto.SignupUserRequest;
import com.smartgroceryApp.smartgrocery.common.auth.dto.UserRefreshTokenRequest;
import com.smartgroceryApp.smartgrocery.common.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AuthUserResponse> signup(@Valid @RequestBody SignupUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUserResponse> login(@Valid @RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthUserResponse> refreshToken(@Valid @RequestBody UserRefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
