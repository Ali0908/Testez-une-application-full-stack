package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtResponseUnitTest {

    private JwtResponse jwtResponse;

    @BeforeEach
    public void setUp() {
        // Arrange: Creating a JwtResponse object with sample data
        jwtResponse = new JwtResponse("sampleToken", 1L, "sampleUsername", "John", "Doe", true);
    }

    @Test
    public void testJwtResponseConstructor() {
        // Assert: Verifying that the constructor correctly sets the values
        assertEquals("sampleToken", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("sampleUsername", jwtResponse.getUsername());
        assertEquals("John", jwtResponse.getFirstName());
        assertEquals("Doe", jwtResponse.getLastName());
        assertEquals(true, jwtResponse.getAdmin());
        assertEquals("Bearer", jwtResponse.getType());  // Default value for type
    }

    @Test
    public void testSettersAndGetters() {
        // Act: Modifying the values using setters
        jwtResponse.setToken("newToken");
        jwtResponse.setId(2L);
        jwtResponse.setUsername("newUsername");
        jwtResponse.setFirstName("Jane");
        jwtResponse.setLastName("Smith");
        jwtResponse.setAdmin(false);

        // Assert: Verifying that the setters correctly updated the values
        assertEquals("newToken", jwtResponse.getToken());
        assertEquals(2L, jwtResponse.getId());
        assertEquals("newUsername", jwtResponse.getUsername());
        assertEquals("Jane", jwtResponse.getFirstName());
        assertEquals("Smith", jwtResponse.getLastName());
        assertEquals(false, jwtResponse.getAdmin());
        assertEquals("Bearer", jwtResponse.getType());  // Type should remain "Bearer"
    }
}
