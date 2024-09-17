package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherDtoIntegrationTest {

    private Validator validator;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        createdAt = LocalDateTime.parse("2021-09-01T00:00:00");
        updatedAt = LocalDateTime.parse("2021-09-01T00:00:00");
    }

    @Test
    public void testValidTeacherDto() {
        // Arrange: Create a valid TeacherDto instance
        TeacherDto teacherDto = new TeacherDto(
                1L,
                "Smith",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act: Validate the TeacherDto
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);

        // Assert: There should be no violations for a valid TeacherDto
        assertTrue(violations.isEmpty(), "Expected no validation violations for a valid TeacherDto");
    }

    @Test
    public void testBlankLastName() {
        // Arrange: Create a TeacherDto instance with a blank last name
        TeacherDto teacherDto = new TeacherDto(
                1L,
                "", // Invalid: Blank last name
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act: Validate the TeacherDto
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);

        // Assert: There should be a violation for the last name
        assertFalse(violations.isEmpty(), "Expected validation violations for a blank last name");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")), "Expected violation for blank last name");
    }

    @Test
    public void testBlankFirstName() {
        // Arrange: Create a TeacherDto instance with a blank first name
        TeacherDto teacherDto = new TeacherDto(
                1L,
                "Smith",
                "", // Invalid: Blank first name
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act: Validate the TeacherDto
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);

        // Assert: There should be a violation for the first name
        assertFalse(violations.isEmpty(), "Expected validation violations for a blank first name");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")), "Expected violation for blank first name");
    }

    @Test
    public void testLastNameSizeExceedsLimit() {
        // Arrange: Create a TeacherDto instance with a last name longer than 20 characters
        TeacherDto teacherDto = new TeacherDto(
                1L,
                "ThisIsAVeryLongLastNameExceedingLimit",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act: Validate the TeacherDto
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);

        // Assert: There should be a violation for the last name size
        assertFalse(violations.isEmpty(), "Expected validation violations for last name size exceeding limit");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")), "Expected violation for last name size exceeding limit");
    }

    @Test
    public void testFirstNameSizeExceedsLimit() {
        // Arrange: Create a TeacherDto instance with a first name longer than 20 characters
        TeacherDto teacherDto = new TeacherDto(
                1L,
                "Smith",
                "ThisIsAVeryLongFirstNameExceedingLimit",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act: Validate the TeacherDto
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);

        // Assert: There should be a violation for the first name size
        assertFalse(violations.isEmpty(), "Expected validation violations for first name size exceeding limit");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")), "Expected violation for first name size exceeding limit");
    }

    @Test
    public void testEquals() {
        // Arrange: Create two equal TeacherDto objects
        TeacherDto teacherDto1 = new TeacherDto(1L, "Smith", "John", createdAt, updatedAt);
        TeacherDto teacherDto2 = new TeacherDto(1L, "Smith", "John", createdAt, updatedAt);

        // Assert: Equal objects should be equal
        assertTrue(teacherDto1.equals(teacherDto1));  // Self-equality check
        assertTrue(teacherDto1.equals(teacherDto2));
        assertTrue(teacherDto2.equals(teacherDto1));  // Symmetry

        // Modify a field to make them unequal
        teacherDto2.setLastName("Doe");

        // Assert: Unequal objects should not be equal
        assertFalse(teacherDto1.equals(teacherDto2));
        assertFalse(teacherDto2.equals(teacherDto1));
    }

    @Test
    public void testHashCodeConsistency() {
        // Arrange: Create two equal TeacherDto objects
        TeacherDto teacherDto1 = new TeacherDto(1L, "Smith", "John", createdAt, updatedAt);
        TeacherDto teacherDto2 = new TeacherDto(1L, "Smith", "John",createdAt, updatedAt);

        // Assert: Equal objects should have the same or similar hash codes
        assertEquals(teacherDto1.hashCode(), teacherDto2.hashCode());

        // Modify a field to make them unequal
        teacherDto2.setLastName("Doe");

        // Assert: Unequal objects may have different hash codes
        assertNotEquals(teacherDto1.hashCode(), teacherDto2.hashCode());
    }

    @Test
    public void testToString() {
        TeacherDto teacherDto = new TeacherDto(1L, "Smith", "John", LocalDateTime.now(), LocalDateTime.now());
        String expectedString = "TeacherDto(id=1, lastName=Smith, firstName=John, createdAt=" + teacherDto.getCreatedAt() + ", updatedAt=" + teacherDto.getUpdatedAt() + ")";

        // Assert: The toString method should return a meaningful representation of the object
        assertTrue(teacherDto.toString().contains(expectedString));
    }

    @Test
    public void testCanEqual() {
        TeacherDto teacherDto = new TeacherDto();

        // Assert: canEqual should return true for the same class
        assertTrue(teacherDto.canEqual(teacherDto));

        // Assert: canEqual should return false for different classes
        assertFalse(teacherDto.canEqual(new Object()));
    }
}
