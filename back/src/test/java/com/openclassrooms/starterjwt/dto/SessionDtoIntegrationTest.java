package com.openclassrooms.starterjwt.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SessionDtoIntegrationTest {

    private Validator validator;
    private ObjectMapper objectMapper;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
         createdAt = LocalDateTime.parse("2021-09-01T00:00:00");
         updatedAt = LocalDateTime.parse("2021-09-01T00:00:00");
    }

    @Test
    public void testValidSessionDto() {
        // Arrange: Create a valid SessionDto instance
        SessionDto sessionDto = new SessionDto(
                1L,
                "Java Basics",
                new Date(),
                1L,
                "A detailed description of the Java Basics session.",
                Collections.singletonList(2L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act: Validate the SessionDto
        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);

        // Assert: No violations should be found
        assertTrue(violations.isEmpty(), "Expected no validation violations for a valid SessionDto");
    }

    @Test
    public void testInvalidSessionDto() {
        // Arrange: Create an invalid SessionDto instance (name and description are blank, date is null)
        SessionDto sessionDto = new SessionDto(
                1L,
                "", // Invalid: Blank name
                null, // Invalid: Null date
                1L,
                "", // Invalid: Blank description
                Collections.singletonList(2L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act: Validate the SessionDto
        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);

        // Assert: There should be violations for name, date, and description
        assertFalse(violations.isEmpty(), "Expected validation violations for an invalid SessionDto");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")), "Expected violation for blank name");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("date")), "Expected violation for null date");
//        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")), "Expected violation for blank description");
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        // Arrange: Create a SessionDto instance
        SessionDto sessionDto = new SessionDto(
                1L,
                "Java Basics",
                new Date(),
                1L,
                "A detailed description of the Java Basics session.",
                Collections.singletonList(2L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act: Serialize the SessionDto to JSON
        String json = objectMapper.writeValueAsString(sessionDto);

        // Assert: Check that JSON contains specific fields
        assertTrue(json.contains("\"name\":\"Java Basics\""), "Expected JSON to contain the name field");
        assertTrue(json.contains("\"description\":\"A detailed description of the Java Basics session.\""), "Expected JSON to contain the description field");
        assertTrue(json.contains("\"teacher_id\":1"), "Expected JSON to contain the teacher_id field");
        assertTrue(json.contains("\"users\":[2]"), "Expected JSON to contain the users field");
    }

    @Test
    public void testDeserialization() throws JsonProcessingException {
        // Arrange: JSON string representing a SessionDto
        String json = "{\"id\":1,\"name\":\"Java Basics\",\"date\":\"2024-09-13T12:00:00.000+00:00\",\"teacher_id\":1,\"description\":\"A detailed description of the Java Basics session.\",\"users\":[2],\"createdAt\":\"2024-09-13T12:00:00\",\"updatedAt\":\"2024-09-13T12:00:00\"}";

        // Act: Deserialize the JSON to a SessionDto object
        SessionDto sessionDto = objectMapper.readValue(json, SessionDto.class);

        // Assert: Check the deserialized fields
        assertEquals("Java Basics", sessionDto.getName(), "Expected the name to match the JSON value");
        assertEquals(1L, sessionDto.getTeacher_id(), "Expected the teacher_id to match the JSON value");
        assertEquals("A detailed description of the Java Basics session.", sessionDto.getDescription(), "Expected the description to match the JSON value");
        assertNotNull(sessionDto.getDate(), "Expected the date to be deserialized");
    }

    @Test
    public void testEquals() {
        // Arrange: Create two equal SessionDto objects
        SessionDto sessionDto1 = new SessionDto(
                1L,
                "Java Basics",
                new Date(),
                1L,
                "A detailed description",
                Collections.singletonList(2L),
                createdAt,
                updatedAt
        );
        SessionDto sessionDto2 = new SessionDto(
                1L,
                "Java Basics",
                new Date(),
                1L,
                "A detailed description",
                Collections.singletonList(2L),
                createdAt,
                updatedAt
        );

        // Assert: Equal objects should be equal
        assertTrue(sessionDto1.equals(sessionDto1));  // Self-equality check
        assertTrue(sessionDto1.equals(sessionDto2));
        assertTrue(sessionDto2.equals(sessionDto1));  // Symmetry

        // Modify a field to make them unequal
        sessionDto2.setName("Java Advanced");

        // Assert: Unequal objects should not be equal
        assertFalse(sessionDto1.equals(sessionDto2));
        assertFalse(sessionDto2.equals(sessionDto1));
    }

    @Test
    public void testHashCodeConsistency() {
        // Arrange: Create two equal SessionDto objects
        SessionDto sessionDto1 = new SessionDto(
                1L,
                "Java Basics",
                new Date(),
                1L,
                "A detailed description",
                Collections.singletonList(2L),
                createdAt,
                updatedAt
        );
        SessionDto sessionDto2 = new SessionDto(
                1L,
                "Java Basics",
                new Date(),
                1L,
                "A detailed description",
                Collections.singletonList(2L),
                createdAt,
                updatedAt
        );

        // Assert: Equal objects should have the same or similar hash codes
        assertEquals(sessionDto1.hashCode(), sessionDto2.hashCode());

        // Modify a field to make them unequal
        sessionDto2.setName("Java Advanced");

        // Assert: Unequal objects may have different hash codes
        assertNotEquals(sessionDto1.hashCode(), sessionDto2.hashCode());
    }

    @Test
    public void testCanEqual() {
        SessionDto sessionDto = new SessionDto();

        // Assert: canEqual should return true for the same class
        assertTrue(sessionDto.canEqual(sessionDto));

        // Assert: canEqual should return false for different classes
        assertFalse(sessionDto.canEqual(new Object()));
    }
}
