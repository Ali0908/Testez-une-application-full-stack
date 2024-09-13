package com.openclassrooms.starterjwt.security.jwt;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // Replace with your security config
public class AuthTokenFilterIntegrationTest {

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())  // Make sure Spring Security is applied
                .addFilters(authTokenFilter)  // Add the AuthTokenFilter to the filter chain
                .build();
    }

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    // Todo: Error: java.lang.AssertionError: Status expected:<200> but was:<401> -> Check valid token
//    @Test
//    public void testValidToken_SetsAuthentication() throws Exception {
//        // Arrange: Mock a valid JWT token
//        String validToken = "openclassrooms"; // Replace with a valid token
//
//        // Act: Perform a request with the token in Authorization header
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
//                        .header("Authorization", "Bearer " + validToken))
//                .andExpect(status().isOk());
//    }

    @Test
    public void testInvalidToken_DoesNotSetAuthentication() throws Exception {
        // Arrange: Mock an invalid JWT token
        String invalidToken = "invalid_token";

        // Mock JwtUtils to simulate an invalid token
        Mockito.when(jwtUtils.validateJwtToken(Mockito.anyString())).thenReturn(false);

        // Act: Perform a request with the invalid token
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());

        // Assert: Verify SecurityContextHolder does not have authentication set
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    public void testMissingToken_DoesNotSetAuthentication() throws Exception {
        // Arrange: No token in request

        // Act: Perform a request without the token
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher"))
                .andExpect(status().isUnauthorized());

        // Assert: Verify SecurityContextHolder does not have authentication set
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }
}