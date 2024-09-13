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



}

