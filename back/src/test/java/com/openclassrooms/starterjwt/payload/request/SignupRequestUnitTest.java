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

    @Test
    public void testEquals() {
        // Arrange
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("valid@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("valid@example.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        // Act & Assert
        assertTrue(signupRequest1.equals(signupRequest2), "SignupRequests with the same fields should be equal");
        assertTrue(signupRequest1.equals(signupRequest1), "SignupRequest should be equal to itself");
        assertFalse(signupRequest1.equals(null), "SignupRequest should not be equal to null");
        assertFalse(signupRequest1.equals(new Object()), "SignupRequest should not be equal to an object of another class");
    }

    @Test
    public void testHashCode() {
        // Arrange
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("valid@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("valid@example.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        // Act & Assert
        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "SignupRequests with the same fields should have the same hashCode");
    }

    @Test
    public void testToString() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Act
        String expectedString = "SignupRequest(email=valid@example.com, firstName=John, lastName=Doe, password=password123)";

        // Assert
        assertEquals(expectedString, signupRequest.toString(), "toString should return the correct representation");
    }

    @Test
    public void testCanEqual() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();

        // Act & Assert
        assertTrue(signupRequest.canEqual(new SignupRequest()), "canEqual should return true for SignupRequest");
        assertFalse(signupRequest.canEqual(new Object()), "canEqual should return false for non-SignupRequest objects");
    }


}
