package com.openclassrooms.starterjwt.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDtoIntegrationTest {

    private Validator validator;
    private ObjectMapper objectMapper;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule to handle LocalDateTime
         createdAt = LocalDateTime.parse("2021-09-01T00:00:00");
         updatedAt = LocalDateTime.parse("2021-09-01T00:00:00");
    }

    @Test
    public void testValidUserDto() {
        // Arrange: Create a valid UserDto instance
        UserDto userDto = new UserDto(1L, "test@example.com", "Doe", "John", true, "password123", LocalDateTime.now(), LocalDateTime.now());

        // Act: Validate the UserDto
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // Assert: No violations should be found
        assertTrue(violations.isEmpty(), "Expected no validation violations for a valid UserDto");
    }

    @Test
    public void testInvalidEmail() {
        // Arrange: Create a UserDto with an invalid email
        UserDto userDto = new UserDto(1L, "invalid-email", "Doe", "John", true, "password123", LocalDateTime.now(), LocalDateTime.now());

        // Act: Validate the UserDto
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // Assert: Ensure there is a violation on the email field
        assertFalse(violations.isEmpty(), "Expected validation violations for an invalid email");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")), "Expected email field to have validation errors");
    }

    @Test
    public void testSizeConstraints() {
        // Arrange: Create a UserDto with too long values for fields
        UserDto userDto = new UserDto(1L, "test@example.com", "ThisLastNameIsWayTooLongForTheConstraint", "ThisFirstNameIsWayTooLongForTheConstraint", true, "password123", LocalDateTime.now(), LocalDateTime.now());

        // Act: Validate the UserDto
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // Assert: Ensure there are violations on the size of fields
        assertFalse(violations.isEmpty(), "Expected validation violations for fields exceeding size constraints");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")), "Expected lastName field to have size validation errors");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")), "Expected firstName field to have size validation errors");
    }

    @Test
    public void testPasswordIgnoredInSerialization() throws JsonProcessingException {
        // Arrange: Create a UserDto instance
        UserDto userDto = new UserDto(1L, "test@example.com", "Doe", "John", true, "password123", LocalDateTime.now(), LocalDateTime.now());

        // Act: Serialize the UserDto to JSON
        String json = objectMapper.writeValueAsString(userDto);

        // Assert: Ensure that the password field is not present in the JSON
        assertFalse(json.contains("password"), "Expected password field to be ignored in JSON serialization");
    }

    @Test
    public void testSerializationAndDeserialization() throws JsonProcessingException {
        // Arrange: Create a UserDto instance
        UserDto userDto = new UserDto(1L, "test@example.com", "Doe", "John", true, "password123", LocalDateTime.now(), LocalDateTime.now());

        // Act: Serialize the UserDto to JSON and then deserialize back to UserDto
        String json = objectMapper.writeValueAsString(userDto);
        UserDto deserializedUserDto = objectMapper.readValue(json, UserDto.class);

        // Assert: Ensure that the deserialized object has the expected values
        assertEquals(userDto.getId(), deserializedUserDto.getId(), "Expected ID to match after deserialization");
        assertEquals(userDto.getEmail(), deserializedUserDto.getEmail(), "Expected email to match after deserialization");
        assertEquals(userDto.getLastName(), deserializedUserDto.getLastName(), "Expected last name to match after deserialization");
        assertEquals(userDto.getFirstName(), deserializedUserDto.getFirstName(), "Expected first name to match after deserialization");
        assertEquals(userDto.isAdmin(), deserializedUserDto.isAdmin(), "Expected admin flag to match after deserialization");
        assertNull(deserializedUserDto.getPassword(), "Expected password to be null after deserialization due to @JsonIgnore");
    }

    @Test
    public void testEquals() {
        // Arrange: Create two equal UserDto objects
        UserDto userDto1 = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", createdAt, updatedAt);
        UserDto userDto2 = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", createdAt, updatedAt);

        // Assert: UserDto1 should be equal to itself and userDto2
        assertTrue(userDto1.equals(userDto1));

        // Modify a field in userDto2 to make them unequal
        userDto2.setEmail("different@email.com");

        // Assert: UserDto1 should not be equal to the modified userDto2
        assertFalse(userDto1.equals(userDto2));

        // Test canEqual with unrelated object
        assertFalse(userDto1.canEqual(new Object()));
    }
    @Test
    public void testToString() {
        // Arrange: Create a UserDto object
        UserDto userDto = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", LocalDateTime.now(), LocalDateTime.now());

        // Act: Get the string representation
        String userDtoString = userDto.toString();

        // Assert: The string should contain expected field values
        assertTrue(userDtoString.contains("id=" + userDto.getId()));
        assertTrue(userDtoString.contains("email=" + userDto.getEmail()));
        assertTrue(userDtoString.contains("lastName=" + userDto.getLastName()));
        assertTrue(userDtoString.contains("firstName=" + userDto.getFirstName()));
        assertTrue(userDtoString.contains("admin=" + userDto.isAdmin()));
        assertTrue(userDtoString.contains("password=" + userDto.getPassword()));
        assertTrue(userDtoString.contains("createdAt=" + userDto.getCreatedAt()));
        assertTrue(userDtoString.contains("updatedAt=" + userDto.getUpdatedAt()));
    }

    @Test
    public void testSetters() {
        // Arrange: Create a UserDto object
        UserDto userDto = new UserDto();

        // Act: Set values using setters
        userDto.setEmail("new@email.com");
        userDto.setLastName("NewLast");
        userDto.setFirstName("NewFirst");
        userDto.setAdmin(false);

        // Assert: Verify that fields are updated correctly
        assertEquals("new@email.com", userDto.getEmail());
        assertEquals("NewLast", userDto.getLastName());
        assertEquals("NewFirst", userDto.getFirstName());
        assertFalse(userDto.isAdmin());
    }

    @Test
    public void testEqualsForHashCodeConsistency() {
        // Arrange: Create two equal UserDto objects
        UserDto userDto1 = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", createdAt, updatedAt);
        UserDto userDto2 = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", createdAt, updatedAt);

        // Assert: Equal objects should be equal
        assertTrue(userDto1.equals(userDto1)); // Check self-equality
        assertTrue(userDto1.equals(userDto2));
        assertTrue(userDto2.equals(userDto1));

        // Modify a field to make them unequal
        userDto2.setEmail("different@example.com");

        // Assert: Unequal objects should not be equal
        assertFalse(userDto1.equals(userDto2));
        assertFalse(userDto2.equals(userDto1));
    }

    @Test
    public void testHashCodeConsistency() {
        // Arrange: Create two equal UserDto objects
        UserDto userDto1 = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", createdAt, updatedAt);
        UserDto userDto2 = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", createdAt, updatedAt);

        // Assert: Equal objects should have the same or similar hash codes
        assertEquals(userDto1.hashCode(), userDto2.hashCode());

        // Modify a field to make them unequal
        userDto2.setEmail("different@email.com");

        // Assert: Unequal objects may have different hash codes
        assertNotEquals(userDto1.hashCode(), userDto2.hashCode());
    }
}
