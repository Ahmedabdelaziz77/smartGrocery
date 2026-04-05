package com.smartgroceryApp.smartgrocery.service;

import com.smartgroceryApp.smartgrocery.auth.dto.AuthUserResponse;
import com.smartgroceryApp.smartgrocery.auth.dto.LoginUserRequest;
import com.smartgroceryApp.smartgrocery.auth.dto.SignupUserRequest;
import com.smartgroceryApp.smartgrocery.auth.dto.UserRefreshTokenRequest;
import com.smartgroceryApp.smartgrocery.auth.service.AuthService;
import com.smartgroceryApp.smartgrocery.auth.service.RefreshTokenService;
import com.smartgroceryApp.smartgrocery.common.enums.Role;
import com.smartgroceryApp.smartgrocery.common.exception.DuplicationException;
import com.smartgroceryApp.smartgrocery.common.exception.UnauthorizedException;
import com.smartgroceryApp.smartgrocery.security.jwt.JwtService;
import com.smartgroceryApp.smartgrocery.user.entity.RefreshToken;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import com.smartgroceryApp.smartgrocery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFullName("User Ahmed");
        testUser.setEmail("zozahmed770@gmail.com");
        testUser.setPassword("test1234");
        testUser.setRole(Role.USER);
        testUser.setEnabled(true);
    }

    @Nested
    @DisplayName("Signup")
    class SignupTests {

        @Test
        @DisplayName("should register first user as ADMIN")
        void signup_firstUser_shouldAssignAdminRole() {
            SignupUserRequest request = new SignupUserRequest("Admin Ahmed", "zozahmed770@gmail.com", "test1234");

            when(userRepository.existsByEmail("zozahmed770@gmail.com")).thenReturn(false);
            when(userRepository.count()).thenReturn(0L);
            when(passwordEncoder.encode("test1234")).thenReturn("test1234");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return user;
            });
            when(jwtService.generateAccessToken(anyLong(), anyString(), anyString())).thenReturn("access-token");
            when(jwtService.generateRefreshToken(anyLong(), anyString())).thenReturn("refresh-token");

            AuthUserResponse response = authService.signup(request);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            assertThat(userCaptor.getValue().getRole()).isEqualTo(Role.ADMIN);
            assertThat(response.getRole()).isEqualTo("ADMIN");
        }

        @Test
        @DisplayName("should register subsequent users as USER")
        void signup_subsequentUser_shouldAssignUserRole() {
            SignupUserRequest request = new SignupUserRequest("User Ahmed", "zozahmed770@gmail.com", "test1234");

            when(userRepository.existsByEmail("zozahmed770@gmail.com")).thenReturn(false);
            when(userRepository.count()).thenReturn(1L);
            when(passwordEncoder.encode("test1234")).thenReturn("test1234");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(2L);
                return user;
            });
            when(jwtService.generateAccessToken(anyLong(), anyString(), anyString())).thenReturn("access-token");
            when(jwtService.generateRefreshToken(anyLong(), anyString())).thenReturn("refresh-token");

            AuthUserResponse response = authService.signup(request);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            assertThat(userCaptor.getValue().getRole()).isEqualTo(Role.USER);
            assertThat(response.getRole()).isEqualTo("USER");
        }

        @Test
        @DisplayName("should throw DuplicateResourceException for existing email")
        void signup_duplicateEmail_shouldThrow() {
            SignupUserRequest request = new SignupUserRequest("User Ahmed", "zozahmed770@gmail.com", "test1234");

            when(userRepository.existsByEmail("zozahmed770@gmail.com")).thenReturn(true);

            assertThatThrownBy(() -> authService.signup(request))
                    .isInstanceOf(DuplicationException.class)
                    .hasMessage("email is registered try a new email!");

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("should normalize email to lowercase")
        void signup_shouldNormalizeEmail() {
            SignupUserRequest request = new SignupUserRequest("User Ahmed", "ZOZAHMED770@GMAIL.COM", "test1234");

            when(userRepository.existsByEmail("zozahmed770@gmail.com")).thenReturn(false);
            when(userRepository.count()).thenReturn(1L);
            when(passwordEncoder.encode("test1234")).thenReturn("test1234");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return user;
            });
            when(jwtService.generateAccessToken(anyLong(), anyString(), anyString())).thenReturn("access-token");
            when(jwtService.generateRefreshToken(anyLong(), anyString())).thenReturn("refresh-token");

            authService.signup(request);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            assertThat(userCaptor.getValue().getEmail()).isEqualTo("zozahmed770@gmail.com");
        }

        @Test
        @DisplayName("should return tokens and user info on successful signup")
        void signup_success_shouldReturnAuthResponse() {
            SignupUserRequest request = new SignupUserRequest("User Ahmed", "zozahmed770@gmail.com", "test1234");

            when(userRepository.existsByEmail("zozahmed770@gmail.com")).thenReturn(false);
            when(userRepository.count()).thenReturn(1L);
            when(passwordEncoder.encode("test1234")).thenReturn("test1234");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return user;
            });
            when(jwtService.generateAccessToken(1L, "zozahmed770@gmail.com", "USER")).thenReturn("access-token");
            when(jwtService.generateRefreshToken(1L, "zozahmed770@gmail.com")).thenReturn("refresh-token");

            AuthUserResponse response = authService.signup(request);

            assertThat(response.getUserId()).isEqualTo(1L);
            assertThat(response.getFullName()).isEqualTo("User Ahmed");
            assertThat(response.getEmail()).isEqualTo("zozahmed770@gmail.com");
            assertThat(response.getAccessToken()).isEqualTo("access-token");
            assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
            verify(refreshTokenService).createRefreshToken(any(User.class), eq("refresh-token"));
        }
    }

    @Nested
    @DisplayName("Login")
    class LoginTests {

        @Test
        @DisplayName("should throw UnauthorizedException for disabled account")
        void login_disabledAccount_shouldThrow() {
            LoginUserRequest req = new LoginUserRequest();
            req.email = "zozahmed770@gmail.com";
            req.password = "test1234";
            testUser.setEnabled(false);

            when(userRepository.findByEmail("zozahmed770@gmail.com")).thenReturn(Optional.of(testUser));

            assertThatThrownBy(() -> authService.login(req))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("user account is disabled or doesn't exist!");
        }

        @Test
        @DisplayName("should throw UnauthorizedException for non-existent user")
        void login_nonExistentUser_shouldThrow() {

            LoginUserRequest req = new LoginUserRequest();
            req.email = "zozahmed770@gmail.com";
            req.password = "test1234";

            when(userRepository.findByEmail("zozahmed770@gmail.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.login(req))
                    .isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("should authenticate and return tokens on valid login")
        void login_success_shouldReturnAuthResponse() {
            LoginUserRequest req = new LoginUserRequest();

            req.email = "zozahmed770@gmail.com";
            req.password = "test1234";

            when(userRepository.findByEmail("zozahmed770@gmail.com")).thenReturn(Optional.of(testUser));
            when(jwtService.generateAccessToken(1L, "zozahmed770@gmail.com", "USER")).thenReturn("access-token");
            when(jwtService.generateRefreshToken(1L, "zozahmed770@gmail.com")).thenReturn("refresh-token");

            AuthUserResponse response = authService.login(req);

            assertThat(response.getUserId()).isEqualTo(1L);
            assertThat(response.getEmail()).isEqualTo("zozahmed770@gmail.com");
            assertThat(response.getAccessToken()).isEqualTo("access-token");
            verify(authenticationManager).authenticate(any());
            verify(refreshTokenService).revokeAllUserTokens(testUser);
        }
    }

    @Nested
    @DisplayName("Refresh Token")
    class RefreshTokenTests {

        @Test
        @DisplayName("should issue new tokens on valid refresh")
        void refreshToken_valid_shouldReturnNewTokens() {
            UserRefreshTokenRequest req = new UserRefreshTokenRequest();

            req.refreshToken = "old-refresh-token";

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(testUser);
            refreshToken.setToken("old-refresh-token");

            when(refreshTokenService.getValidToken("old-refresh-token")).thenReturn(refreshToken);
            when(jwtService.extractTokenType("old-refresh-token")).thenReturn("refresh");
            when(jwtService.isTokenExpired("old-refresh-token")).thenReturn(false);
            when(jwtService.generateAccessToken(1L, "zozahmed770@gmail.com", "USER")).thenReturn("new-access-token");
            when(jwtService.generateRefreshToken(1L, "zozahmed770@gmail.com")).thenReturn("new-refresh-token");

            AuthUserResponse response = authService.refreshToken(req);

            assertThat(response.getAccessToken()).isEqualTo("new-access-token");
            assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");
            verify(refreshTokenService).revokeToken(refreshToken);
            verify(refreshTokenService).createRefreshToken(testUser, "new-refresh-token");
        }
    }
}
