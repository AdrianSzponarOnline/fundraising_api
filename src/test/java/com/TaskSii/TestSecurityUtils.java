package com.TaskSii;

import com.TaskSii.service.JWTService;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

public class TestSecurityUtils {
    public static String mockToken = "mock-jwt-token";

    public static void setupJwtMocks(JWTService jwtService, String username, List<String> roles) {
        Mockito.when(jwtService.extractUsername(mockToken)).thenReturn(username);
        Mockito.when(jwtService.extractRoles(mockToken)).thenReturn(new ArrayList<>(roles));
        Mockito.when(jwtService.isTokenValid(Mockito.eq(mockToken), Mockito.any())).thenReturn(true);
    }
    public static MockHttpServletRequestBuilder addAuthHeader(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder
                .header("Authorization", "Bearer " + mockToken)
                .with(csrf());
    }
}
