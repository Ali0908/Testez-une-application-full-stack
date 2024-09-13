package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilsIntegrationTest {

    @Autowired
    private JwtUtils jwtUtils;

    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        // Mock user details and authentication
        UserDetailsImpl userDetails = new UserDetailsImpl(3L, "louis@test.com", "password", "louis@test.com", true, "password");
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    public void testGenerateJwtToken() {
        // Act: Generate the JWT token
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert: Token should not be null
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));  // JWT tokens typically start with this prefix
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        // Arrange: Generate a JWT token
        String token = jwtUtils.generateJwtToken(authentication);

        // Act: Extract the username from the token
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Assert: Username should match the one in authentication
        assertEquals("louis@test.com", username);
    }

    @Test
    public void testValidateJwtToken_ValidToken() {
        // Arrange: Generate a valid JWT token
        String token = jwtUtils.generateJwtToken(authentication);

        // Act: Validate the token
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert: The token should be valid
        assertTrue(isValid);
    }

    @Test
    public void testValidateJwtToken_InvalidToken() {
        // Arrange: Create an invalid token
        String invalidToken = "A&sjsbqid";

        // Act: Validate the token
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Assert: The token should be invalid
        assertFalse(isValid);
    }

    @Test
    public void testValidateJwtToken_ExpiredToken() throws InterruptedException {
        // Arrange: Create a new JwtUtils instance with a short expiration time
        JwtUtils tempJwtUtils = new JwtUtils();
        tempJwtUtils.jwtSecret = jwtUtils.jwtSecret;
        tempJwtUtils.jwtExpirationMs = 1; // Set token expiration to 1 ms for test purposes
        String token = tempJwtUtils.generateJwtToken(authentication);

        // Wait for the token to expire
        Thread.sleep(10);

        // Act: Validate the expired token
        boolean isValid = tempJwtUtils.validateJwtToken(token);

        // Assert: The token should be expired and therefore invalid
        assertFalse(isValid);
    }
}
