package com.openclassrooms.starterjwt.payload;


import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginRequestUnitTest {


    @Test
    public void testGetEmail() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");

        // Act
        String email = loginRequest.getEmail();

        // Assert
        assertEquals("test@example.com", email);
    }


    @Test
    public void testSetEmail() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();

        // Act
        loginRequest.setEmail("newemail@example.com");

        // Assert
        assertEquals("newemail@example.com", loginRequest.getEmail());
    }

    @Test
    public void testGetPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password123");

        // Act
        String password = loginRequest.getPassword();

        // Assert
        assertEquals("password123", password);
    }

    @Test
    public void testSetPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();

        // Act
        loginRequest.setPassword("newPassword");

        // Assert
        assertEquals("newPassword", loginRequest.getPassword());
    }
}
