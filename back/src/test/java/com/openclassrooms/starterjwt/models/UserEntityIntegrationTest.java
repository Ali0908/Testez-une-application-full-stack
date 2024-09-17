package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

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
        // Arrange: Create users with same and different IDs
        User user1 = User.builder()
                .id(1L)
                .email("user1@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .build();
        User user2 = User.builder()
                .id(1L)  // Same ID as user1
                .email("user2@example.com")
                .firstName("Paul")
                .lastName("Smith")
                .password("password456")
                .build();
        User user3 = User.builder()
                .id(2L)  // Different ID
                .email("user3@example.com")
                .firstName("Sam")
                .lastName("Brown")
                .password("password789")
                .build();

        // Assert: Equals and hashCode should consider only "id"

        // **Equals:**
        assertEquals(user1, user2); // Equal due to same id
        assertNotEquals(user1, user3); // Not equal due to different id
        assertNotEquals(user2, user3); // Not equal due to different id (even though other fields are same)

        // **HashCode:**
        assertEquals(user1.hashCode(), user2.hashCode()); // Same hash code due to same id
        assertNotEquals(user1.hashCode(), user3.hashCode()); // Different hash codes due to different ids
    }

    @Test
    public void testEqualsAndHashCodeWithDataAnnotation() {
        // Arrange: Create users with same and different IDs
        User user1 = User.builder()
                .id(1L)
                .email("user1@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .build();
        User user2 = User.builder()
                .id(1L)  // Same ID as user1
                .email("user2@example.com")
                .firstName("Paul")
                .lastName("Smith")
                .password("password456")
                .build();
        User user3 = User.builder()
                .id(2L)  // Different ID
                .email("user3@example.com")
                .firstName("Sam")
                .lastName("Brown")
                .password("password789")
                .build();

        // Assert: Equals and hashCode should consider only "id"

        // **Equals:**
        assertEquals(user1, user2); // Equal due to same id
        assertNotEquals(user1, user3); // Not equal due to different id
        assertNotEquals(user2, user3); // Not equal due to different id (even though other fields are same)

        // **HashCode:**
        assertEquals(user1.hashCode(), user2.hashCode()); // Same hash code due to same id
        assertNotEquals(user1.hashCode(), user3.hashCode()); // Different hash codes due to different ids
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

    @Test
    public void testUserFieldValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Arrange: Create a user with invalid data
        User user = new User();
        user.setEmail("invalid-email"); // Invalid email
        user.setFirstName("");          // Empty first name
        user.setLastName("L");          // Assuming there's a size constraint on lastName
        user.setPassword("123");        // Assuming there's a size constraint on password

        // Act: Validate the user object
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert: There should be violations
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testRequiredArgsConstructor() {
        // Arrange: Create a user object with required arguments
        User user = new User("john.doe@example.com", "Doe", "John", "password123", true);

        // Assert: Verify that the required fields are set correctly
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testRequiredArgsConstructor_MissingField() {
        // Arrange: Attempt to create a user with a missing required field (assuming email is required)
        try {
            new User(null, "Doe", "John", "password123", true); // Missing email
            fail("Expected an exception when a required field is missing.");
        } catch (Exception e) {
            // Assert: An exception should be thrown when a required field is missing
            assertInstanceOf(NullPointerException.class, e); // Adapt assertion based on expected exception type
        }
    }

    @Test
    public void testUserObjectCreation_ValidFields() {
        // Arrange: Values for user creation
        long id = 5000L;
        String email = "marion@test.com";
        String firstName = "Marion";
        String lastName = "Test";
        String password = "password123";
        boolean active = true;
        LocalDateTime createdDate = LocalDateTime.now();
        LocalDateTime updatedDate = LocalDateTime.now();

        // Act: Create a user object
        User user = new User(id, email, lastName , firstName, password, active, createdDate, updatedDate);

        // Assert: Verify the object was created with the expected values
        assertNotNull(user, "User object should not be null"); // Check if object is created
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(password, user.getPassword());
        assertEquals(createdDate, user.getCreatedAt());
        assertEquals(updatedDate, user.getUpdatedAt());
    }

//    @Test
//    public void testUserObjectCreation_EmailExceedsMaxLength() {
//        // Arrange: Values for user creation with an email exceeding max length
//        String email = "this.is.a.very.long.email.address@example.com"; // Length: 51
//        String firstName = "John";
//        String lastName = "Doe";
//        String password = "password123";
//        boolean active = true;
//
//        // Act: Attempt to create a user object
//        try {
//            new User(email, lastName, firstName, password, active);
//            fail("Expected an exception when email exceeds max length.");
//        } catch (Exception e) {
//
//            System.out.println(e.getClass().getName());
//            // Assert: An exception should be thrown when email size is greater than 50
////            assertInstanceOf(ConstraintViolationException.class, e); // Assuming @Email annotation throws this exception
//        }
//    }
}

