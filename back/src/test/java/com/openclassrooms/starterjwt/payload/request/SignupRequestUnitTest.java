package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestUnitTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidSignupRequest() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no constraint violations");
    }

    @Test
    public void testInvalidEmail() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid-email");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty(), "There should be constraint violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void testBlankFirstName() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@example.com");
        signupRequest.setFirstName("");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty(), "There should be constraint violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    public void testShortPassword() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("123");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty(), "There should be constraint violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }
}
