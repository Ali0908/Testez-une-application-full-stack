package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserEntityIntegrationTest {

    @Test
    public void testConstructor_AllArgs() {
        // Arrange: Create a user using the all-args constructor
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "john.doe@example.com", "Doe", "John", "password123", true, now, now);

        // Assert: Ensure that all fields are correctly set
        assertEquals(1L, user.getId());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    public void testConstructor_RequiredArgs() {
        // Arrange: Create a user using the required-args constructor
        User user = new User("jane.doe@example.com", "Doe", "Jane", "password123", false);

        // Assert: Ensure that only required fields are set correctly
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("Jane", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
    }
    @Test
    public void testSetters() {
        // Arrange: Create an empty user object
        User user = new User();

        // Act: Set the fields using the setter methods
        user.setEmail("john.doe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // Assert: Check if the values are set correctly
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    public void testEquals_SameId() {
        // Arrange: Create two users with the same id
        User user1 = new User(1L, "john.doe@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(1L, "jane.doe@example.com", "Doe", "Jane", "password123", false, LocalDateTime.now(), LocalDateTime.now());

        // Assert: They should be equal since they have the same id
        assertEquals(user1, user2);
    }

    @Test
    public void testEquals_DifferentId() {
        // Arrange: Create two users with different ids
        User user1 = new User(1L, "john.doe@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(2L, "jane.doe@example.com", "Doe", "Jane", "password123", false, LocalDateTime.now(), LocalDateTime.now());

        // Assert: They should not be equal since they have different ids
        assertNotEquals(user1, user2);
    }

    @Test
    public void testHashCode() {
        // Arrange: Create two users with the same id
        User user1 = new User(1L, "john.doe@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(1L, "jane.doe@example.com", "Doe", "Jane", "password123", false, LocalDateTime.now(), LocalDateTime.now());

        // Assert: They should have the same hash code since they have the same id
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testToString() {
        // Arrange: Create a user
        User user = new User(1L, "john.doe@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());

        // Act: Convert the user to a string
        String userString = user.toString();

        // Assert: The string should contain the user's id and email
        assertTrue(userString.contains("1"));
        assertTrue(userString.contains("john.doe@example.com"));
    }

    @Test
    public void testCanEqual() {
        // Arrange: Create a user and another object
        User user = new User(1L, "john.doe@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        Object anotherObject = new Object();

        // Assert: The user should not be equal to a different object type
        assertFalse(user.canEqual(anotherObject));

        // Assert: The user should be equal to another user
        User anotherUser = new User(1L, "jane.doe@example.com", "Doe", "Jane", "password123", false, LocalDateTime.now(), LocalDateTime.now());
        assertTrue(user.canEqual(anotherUser));
    }
    @Test
    public void testUserBuilder_BuildCompleteUser() {
        // Arrange
        String email = "john.doe@example.com";
        String lastName = "Doe";
        String firstName = "John";
        String password = "password123";

        User user = User.builder()
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .build();

        // Assert: User object should have the set values
        assertEquals(email, user.getEmail());
        assertEquals(lastName, user.getLastName());
        assertEquals(firstName, user.getFirstName());
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testUserBuilder_ToString() {
        // Arrange
        String email = "john.doe@example.com";
        String password = "password123";
        String lastName = "Doe";
        String firstName = "John";

        User user = User.builder()
                .email(email)
                .password(password)
                .lastName(lastName)
                .firstName(firstName)
                .build();

        // Act
        String userString = user.toString();

        // Assert: String should contain expected information (adapt based on expected format)
        assertTrue(userString.contains(email));
        assertTrue(userString.contains(lastName));
        assertTrue(userString.contains(firstName)); // Adjust if not included
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = User.builder()
                .id(1L)
                .email("user1@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .build();
        User user2 = User.builder()
                .id(1L)
                .email("user2@example.com")
                .firstName("Paul")
                .lastName("Smith")
                .password("password456")
                .build();
        User user3 = User.builder()
                .id(2L)
                .email("user3@example.com")
                .firstName("Sam")
                .lastName("Brown")
                .password("password789")
                .build();

        assertEquals(user1, user2); // Equal due to same id
        assertNotEquals(user1, user3); // Not equal due to different id
    }

    @Test
    public void testBuilder() {
        User user = User.builder()
                .id(1L)
                .email("user3@example.com")
                .firstName("Sam")
                .lastName("Brown")
                .password("password789")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("user3@example.com", user.getEmail());
        assertEquals("Sam", user.getFirstName());
        assertEquals("Brown", user.getLastName());
    }
}

