package com.todolist.security;

import com.todolist.service.JwtService;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JWT Authentication Filter Unit Tests
 *
 * Tests for JwtAuthenticationFilter covering:
 * - Token extraction from request header
 * - Token validation
 * - Authentication context setup
 * - Filter chain continuation
 * - Error handling for invalid tokens
 *
 * @author TodoList Team
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JWT Authentication Filter Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String validToken = "valid.jwt.token";
    private final Long userId = 1L;
    private final String username = "testuser";

    @BeforeEach
    void setUp() {
        // Clear security context before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should successfully authenticate with valid token")
    void doFilterInternal_ValidToken() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.validateToken(validToken)).thenReturn(true);
        when(jwtService.getUserIdFromToken(validToken)).thenReturn(userId);
        when(jwtService.getUsernameFromToken(validToken)).thenReturn(username);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).validateToken(validToken);
        verify(jwtService).getUserIdFromToken(validToken);
        verify(jwtService).getUsernameFromToken(validToken);
        verify(filterChain).doFilter(request, response);

        SecurityContext context = SecurityContextHolder.getContext();
        org.junit.jupiter.api.Assertions.assertNotNull(context.getAuthentication());
        org.junit.jupiter.api.Assertions.assertEquals(userId, context.getAuthentication().getPrincipal());
        org.junit.jupiter.api.Assertions.assertEquals(username, context.getAuthentication().getCredentials());
    }

    @Test
    @DisplayName("Should continue filter chain without authentication when no token provided")
    void doFilterInternal_NoToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);

        SecurityContext context = SecurityContextHolder.getContext();
        org.junit.jupiter.api.Assertions.assertNull(context.getAuthentication());
    }

    @Test
    @DisplayName("Should continue filter chain when authorization header is empty")
    void doFilterInternal_EmptyAuthHeader() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should continue filter chain when token is invalid")
    void doFilterInternal_InvalidToken() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer invalid.token";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.validateToken("invalid.token")).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).validateToken("invalid.token");
        verify(jwtService, never()).getUserIdFromToken(any());
        verify(jwtService, never()).getUsernameFromToken(any());
        verify(filterChain).doFilter(request, response);

        SecurityContext context = SecurityContextHolder.getContext();
        org.junit.jupiter.api.Assertions.assertNull(context.getAuthentication());
    }

    @Test
    @DisplayName("Should handle token without Bearer prefix")
    void doFilterInternal_TokenWithoutBearerPrefix() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("invalid.token");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);

        SecurityContext context = SecurityContextHolder.getContext();
        org.junit.jupiter.api.Assertions.assertNull(context.getAuthentication());
    }

    @Test
    @DisplayName("Should handle malformed authorization header")
    void doFilterInternal_MalformedAuthHeader() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle authorization header with incorrect prefix")
    void doFilterInternal_IncorrectPrefix() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle multiple requests correctly")
    void doFilterInternal_MultipleRequests() throws ServletException, IOException {
        // First request with valid token
        String authHeader1 = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader1);
        when(jwtService.validateToken(validToken)).thenReturn(true);
        when(jwtService.getUserIdFromToken(validToken)).thenReturn(userId);
        when(jwtService.getUsernameFromToken(validToken)).thenReturn(username);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert first request
        SecurityContext context1 = SecurityContextHolder.getContext();
        org.junit.jupiter.api.Assertions.assertNotNull(context1.getAuthentication());

        // Clear context for second request
        SecurityContextHolder.clearContext();

        // Second request without token
        reset(request);
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert second request
        SecurityContext context2 = SecurityContextHolder.getContext();
        org.junit.jupiter.api.Assertions.assertNull(context2.getAuthentication());
    }

    @Test
    @DisplayName("Should set authentication details correctly")
    void doFilterInternal_AuthenticationDetails() throws ServletException, IOException {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(jwtService.validateToken(validToken)).thenReturn(true);
        when(jwtService.getUserIdFromToken(validToken)).thenReturn(userId);
        when(jwtService.getUsernameFromToken(validToken)).thenReturn(username);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        SecurityContext context = SecurityContextHolder.getContext();
        org.junit.jupiter.api.Assertions.assertNotNull(context.getAuthentication());
        org.junit.jupiter.api.Assertions.assertNotNull(context.getAuthentication().getDetails());
    }

    @Test
    @DisplayName("Should handle authentication with different user IDs")
    void doFilterInternal_DifferentUserIds() throws ServletException, IOException {
        // Arrange
        Long userId1 = 1L;
        Long userId2 = 2L;
        String token1 = "token1";
        String token2 = "token2";

        String authHeader1 = "Bearer " + token1;
        when(request.getHeader("Authorization")).thenReturn(authHeader1);
        when(jwtService.validateToken(token1)).thenReturn(true);
        when(jwtService.getUserIdFromToken(token1)).thenReturn(userId1);
        when(jwtService.getUsernameFromToken(token1)).thenReturn("user1");

        // Act first request
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert first request
        SecurityContext context1 = SecurityContextHolder.getContext();
        org.junit.jupiter.api.Assertions.assertEquals(userId1, context1.getAuthentication().getPrincipal());

        // Clear and setup second request
        SecurityContextHolder.clearContext();
        String authHeader2 = "Bearer " + token2;
        reset(request);
        when(request.getHeader("Authorization")).thenReturn(authHeader2);
        when(jwtService.validateToken(token2)).thenReturn(true);
        when(jwtService.getUserIdFromToken(token2)).thenReturn(userId2);
        when(jwtService.getUsernameFromToken(token2)).thenReturn("user2");

        // Act second request
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert second request
        SecurityContext context2 = SecurityContextHolder.getContext();
        org.junit.jupiter.api.Assertions.assertEquals(userId2, context2.getAuthentication().getPrincipal());
    }

    @Test
    @DisplayName("Should always continue filter chain regardless of authentication")
    void doFilterInternal_AlwaysContinueChain() throws ServletException, IOException {
        // Arrange - Test with valid token
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.validateToken(validToken)).thenReturn(true);
        when(jwtService.getUserIdFromToken(validToken)).thenReturn(userId);
        when(jwtService.getUsernameFromToken(validToken)).thenReturn(username);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert - verify filter chain was called
        verify(filterChain, times(1)).doFilter(request, response);

        // Reset for second test - without token
        SecurityContextHolder.clearContext();
        reset(request, filterChain);
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert - verify filter chain was still called
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
