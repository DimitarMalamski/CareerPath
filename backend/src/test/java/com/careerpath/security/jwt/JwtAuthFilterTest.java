package com.careerpath.security.jwt;

import com.careerpath.domain.port.UserOnboardingPort;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserOnboardingPort userOnboardingPort;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Claims claims;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldSkipWhenNoAuthorizationHeader() throws Exception {
        JwtAuthFilter filter = new JwtAuthFilter(jwtTokenProvider, userOnboardingPort);

        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtTokenProvider, userOnboardingPort);
    }

    @Test
    void doFilterInternal_shouldAuthenticateUser_whenValidTokenProvided() throws Exception {
        JwtAuthFilter filter = new JwtAuthFilter(jwtTokenProvider, userOnboardingPort);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtTokenProvider.validate("valid-token")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("user-123");
        when(claims.get("role")).thenReturn("user");

        filter.doFilterInternal(request, response, filterChain);

        verify(jwtTokenProvider).validate("valid-token");
        verify(userOnboardingPort).ensureUserProfile("user-123");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldClearContext_whenTokenValidationFails() throws Exception {
        JwtAuthFilter filter = new JwtAuthFilter(jwtTokenProvider, userOnboardingPort);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtTokenProvider.validate("invalid-token"))
                .thenThrow(new RuntimeException("Invalid token"));

        filter.doFilterInternal(request, response, filterChain);

        verify(jwtTokenProvider).validate("invalid-token");
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(userOnboardingPort);
    }
}
