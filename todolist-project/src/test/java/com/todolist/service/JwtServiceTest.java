package com.todolist.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JWT Service Unit Tests
 *
 * Tests for JwtService covering:
 * - Token generation (access and refresh tokens)
 * - Token validation
 * - Token parsing and claims extraction
 * - Error handling for invalid tokens
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JWT Service Tests")
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String secret = "test-secret-key-for-jwt-token-generation-and-validation-must-be-long-enough";
    private final Long expiration = 86400L; // 24 hours

    @BeforeEach
    void setUp() {
        // Set up test configuration using reflection
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expiration", expiration);
    }

    @Test
    @DisplayName("Should generate valid access token")
    void generateToken_Success() {
        // Act
        Long userId = 1L;
        String username = "testuser";
        String token = jwtService.generateToken(userId, username);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts

        // Verify token can be parsed
        assertThat(jwtService.validateToken(token)).isTrue();
        assertThat(jwtService.getUserIdFromToken(token)).isEqualTo(userId);
        assertThat(jwtService.getUsernameFromToken(token)).isEqualTo(username);
    }

    @Test
    @DisplayName("Should generate valid refresh token")
    void generateRefreshToken_Success() {
        // Act
        Long userId = 1L;
        String refreshToken = jwtService.generateRefreshToken(userId);

        // Assert
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken).isNotEmpty();
        assertThat(refreshToken.split("\\.")).hasSize(3); // JWT has 3 parts

        // Verify token can be parsed
        assertThat(jwtService.validateToken(refreshToken)).isTrue();
        assertThat(jwtService.getUserIdFromToken(refreshToken)).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should validate valid token successfully")
    void validateToken_ValidToken() {
        // Arrange
        String token = jwtService.generateToken(1L, "testuser");

        // Act
        boolean isValid = jwtService.validateToken(token);

        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should invalidate invalid token")
    void validateToken_InvalidToken() {
        // Act
        boolean isValid = jwtService.validateToken("invalid.token.here");

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should invalidate empty token")
    void validateToken_EmptyToken() {
        // Act
        boolean isValid = jwtService.validateToken("");

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should invalidate null token")
    void validateToken_NullToken() {
        // Act
        boolean isValid = jwtService.validateToken(null);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should invalidate tampered token")
    void validateToken_TamperedToken() {
        // Arrange
        String token = jwtService.generateToken(1L, "testuser");
        String tamperedToken = token + "tampered";

        // Act
        boolean isValid = jwtService.validateToken(tamperedToken);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should extract username from valid token")
    void getUsernameFromToken_Success() {
        // Arrange
        Long userId = 1L;
        String username = "testuser";
        String token = jwtService.generateToken(userId, username);

        // Act
        String extractedUsername = jwtService.getUsernameFromToken(token);

        // Assert
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    @DisplayName("Should throw exception when extracting username from invalid token")
    void getUsernameFromToken_InvalidToken() {
        // Act & Assert
        assertThatThrownBy(() -> jwtService.getUsernameFromToken("invalid.token"))
                .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("Should extract user ID from valid token")
    void getUserIdFromToken_Success() {
        // Arrange
        Long userId = 123L;
        String username = "testuser";
        String token = jwtService.generateToken(userId, username);

        // Act
        Long extractedUserId = jwtService.getUserIdFromToken(token);

        // Assert
        assertThat(extractedUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should extract user ID from refresh token")
    void getUserIdFromToken_RefreshToken() {
        // Arrange
        Long userId = 456L;
        String refreshToken = jwtService.generateRefreshToken(userId);

        // Act
        Long extractedUserId = jwtService.getUserIdFromToken(refreshToken);

        // Assert
        assertThat(extractedUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should throw exception when extracting user ID from invalid token")
    void getUserIdFromToken_InvalidToken() {
        // Act & Assert
        assertThatThrownBy(() -> jwtService.getUserIdFromToken("invalid.token"))
                .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void generateToken_DifferentUsers() {
        // Act
        String token1 = jwtService.generateToken(1L, "user1");
        String token2 = jwtService.generateToken(2L, "user2");

        // Assert
        assertThat(token1).isNotEqualTo(token2);
        assertThat(jwtService.getUserIdFromToken(token1)).isEqualTo(1L);
        assertThat(jwtService.getUserIdFromToken(token2)).isEqualTo(2L);
        assertThat(jwtService.getUsernameFromToken(token1)).isEqualTo("user1");
        assertThat(jwtService.getUsernameFromToken(token2)).isEqualTo("user2");
    }

    @Test
    @DisplayName("Should generate different tokens on each call")
    void generateToken_DifferentCalls() {
        // Arrange
        Long userId = 1L;
        String username = "testuser";

        // Act
        String token1 = jwtService.generateToken(userId, username);
        // Small delay to ensure different iat (issued at) claim
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String token2 = jwtService.generateToken(userId, username);

        // Assert
        assertThat(token1).isNotEqualTo(token2);
        // Both should be valid and have same claims except iat
        assertThat(jwtService.getUserIdFromToken(token1)).isEqualTo(jwtService.getUserIdFromToken(token2));
        assertThat(jwtService.getUsernameFromToken(token1)).isEqualTo(jwtService.getUsernameFromToken(token2));
    }

    @Test
    @DisplayName("Should handle special characters in username")
    void generateToken_SpecialCharacters() {
        // Act
        String username = "user+test@example.com";
        String token = jwtService.generateToken(1L, username);

        // Assert
        assertThat(jwtService.validateToken(token)).isTrue();
        assertThat(jwtService.getUsernameFromToken(token)).isEqualTo(username);
    }

    @Test
    @DisplayName("Should handle long username")
    void generateToken_LongUsername() {
        // Arrange
        StringBuilder longUsername = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longUsername.append("a");
        }
        String username = longUsername.toString();

        // Act
        String token = jwtService.generateToken(1L, username);

        // Assert
        assertThat(jwtService.validateToken(token)).isTrue();
        assertThat(jwtService.getUsernameFromToken(token)).isEqualTo(username);
    }

    @Test
    @DisplayName("Should generate tokens with correct structure")
    void generateToken_TokenStructure() {
        // Act
        String token = jwtService.generateToken(1L, "testuser");

        // Assert
        String[] parts = token.split("\\.");
        assertThat(parts).hasSize(3);
        assertThat(parts[0]).isNotEmpty(); // Header
        assertThat(parts[1]).isNotEmpty(); // Payload
        assertThat(parts[2]).isNotEmpty(); // Signature
    }
}
