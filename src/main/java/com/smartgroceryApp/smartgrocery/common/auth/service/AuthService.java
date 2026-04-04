package com.smartgroceryApp.smartgrocery.common.auth.service;

import com.smartgroceryApp.smartgrocery.common.auth.dto.AuthUserResponse;
import com.smartgroceryApp.smartgrocery.common.auth.dto.LoginUserRequest;
import com.smartgroceryApp.smartgrocery.common.auth.dto.SignupUserRequest;
import com.smartgroceryApp.smartgrocery.common.enums.Role;
import com.smartgroceryApp.smartgrocery.common.exception.DuplicationException;
import com.smartgroceryApp.smartgrocery.common.exception.UnauthorizedException;
import com.smartgroceryApp.smartgrocery.common.util.CurrentUserService;
import com.smartgroceryApp.smartgrocery.security.jwt.JwtService;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import com.smartgroceryApp.smartgrocery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CurrentUserService currentUserService;

    public AuthUserResponse signup(SignupUserRequest request) {
        String email = request.email.trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicationException("email is registered try a new email!");
        }

        User user = new User();
        user.setFullName(request.fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password));

        // first user gets admin role automatically
        user.setRole(userRepository.count() == 0 ? Role.ADMIN : Role.USER);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().name());

        String refreshTokenValue = jwtService.generateRefreshToken(
                savedUser.getId(),
                savedUser.getEmail());

        refreshTokenService.createRefreshToken(savedUser, refreshTokenValue);

        log.info("new user registered successfully with email {}", savedUser.getEmail());

        return new AuthUserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                accessToken,
                refreshTokenValue);
    }


    public AuthUserResponse login(LoginUserRequest request) {
        String email = request.email.trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("invalid email or password!!"));

        if (!user.isEnabled()) {
            throw new UnauthorizedException("user account is disabled or doesn't exist!");
        }

        // revoke old tokens before creating new ones (:
        refreshTokenService.revokeAllUserTokens(user);

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name());

        String refreshTokenValue = jwtService.generateRefreshToken(
                user.getId(),
                user.getEmail());

        refreshTokenService.createRefreshToken(user, refreshTokenValue);

        log.info("user logged in with email {}", user.getEmail());

        return new AuthUserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                accessToken,
                refreshTokenValue);
    }
}
