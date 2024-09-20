package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class SessionEntityIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    private Teacher teacher1, teacher2;
    private Session session1, session2, session3;

    @BeforeEach
    public void setUp() {
        // Create Teachers for testing
        teacher1 = new Teacher();
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        // Create Sessions for testing
        session1 = new Session();
        session1.setName("Spring Boot Session");
        session1.setDate(new Date());
        session1.setDescription("Introduction to Spring Boot");
        session1.setTeacher(teacher1);
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());

        session2 = new Session();
        session2.setName("Spring Boot Session");
        session2.setDate(new Date());
        session2.setDescription("Introduction to Spring Boot");
        session2.setTeacher(teacher1);
        session2.setCreatedAt(LocalDateTime.now());
        session2.setUpdatedAt(LocalDateTime.now());

        session3 = new Session();
        session3.setName("Spring Boot Session");
        session3.setDate(new Date());
        session3.setDescription("Introduction to Spring Boot");
        session3.setTeacher(teacher1);
        session3.setCreatedAt(LocalDateTime.now());
        session3.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testEquals() {
        // Equals should return true for equal objects
        assertTrue(session1.equals(session2), "Sessions with the same data should be equal");

        // Modify one session and check if equals returns false
        session3.setId(100L);
        assertFalse(session1.equals(session3), "Sessions with different data should not be equal");
    }

    @Test
    public void testHashCode() {
        // Hash codes should be the same for equal objects
        assertEquals(session1.hashCode(), session2.hashCode(), "Hash codes for equal sessions should be the same");

        // Modify one session. They should have the same hash code since they have the same id
        session2.setTeacher(teacher2);
        assertEquals(session1.hashCode(), session2.hashCode(), "Hash codes are same since they have the same id");
    }

    @Test
    public void testSetTeacher() {
        // Check if teacher is correctly set
        session1.setTeacher(teacher2);
        assertEquals(teacher2, session1.getTeacher(), "The teacher should be correctly updated");
    }

    @Test
    public void testSetCreatedAt() {
        // Check if createdAt is correctly set
        LocalDateTime createdAt = LocalDateTime.of(2023, 9, 10, 10, 30);
        session1.setCreatedAt(createdAt);
        assertEquals(createdAt, session1.getCreatedAt(), "The createdAt field should be correctly updated");
    }

    @Test
    public void testSetUpdatedAt() {
        // Check if updatedAt is correctly set
        LocalDateTime updatedAt = LocalDateTime.of(2023, 9, 10, 10, 30);
        session1.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, session1.getUpdatedAt(), "The updatedAt field should be correctly updated");
    }
//
//    @Test
//    public void testManyToManyRelationshipWithUsers() {
//        // Arrange: Create some users
//        User user1 = new User();
//        user1.setEmail("user1@example.com");
//        user1.setPassword("password1");
//
//        User user2 = new User();
//        user2.setEmail("user2@example.com");
//        user2.setPassword("password2");
//
//        // Persist users
//        entityManager.persist(user1);
//        entityManager.persist(user2);
//
//        // Add users to the session
//        session1.setUsers(List.of(user1, user2));
//
//        // Act: Persist the session
//        entityManager.persist(session1);
//        entityManager.flush();
//        entityManager.clear();
//
//        // Retrieve the session from the database
//        Session retrievedSession = entityManager.find(Session.class, session1.getId());
//
//        // Assert: Check if users are correctly associated
//        assertNotNull(retrievedSession.getUsers());
//        assertEquals(2, retrievedSession.getUsers().size(), "There should be two users associated with the session.");
//    }
//
//    @Test
//    public void testPersistAndRetrieveSession() {
//        // Act: Persist the session
//        entityManager.persist(session1);
//        entityManager.flush();
//        entityManager.clear();
//
//        // Retrieve the session from the database
//        Session retrievedSession = entityManager.find(Session.class, session1.getId());
//
//        // Assert: Check that the retrieved session matches the original session
//        assertNotNull(retrievedSession);
//        assertEquals(session1.getName(), retrievedSession.getName());
//        assertEquals(session1.getDescription(), retrievedSession.getDescription());
//        assertEquals(session1.getTeacher().getFirstName(), retrievedSession.getTeacher().getFirstName());
//    }
//
//    @Test
//    public void testCreatedAtAndUpdatedAt_AuditingFields() {
//        // Act: Persist the session
//        entityManager.persist(session1);
//        entityManager.flush();
//
//        // Assert: Check if createdAt and updatedAt are automatically populated
//        assertNotNull(session1.getCreatedAt(), "The createdAt field should be automatically populated.");
//        assertNotNull(session1.getUpdatedAt(), "The updatedAt field should be automatically populated.");
//
//        // Store the original updatedAt
//        LocalDateTime originalUpdatedAt = session1.getUpdatedAt();
//
//        // Act: Update the session and flush
//        session1.setDescription("Updated Description");
//        entityManager.flush();
//
//        // Assert: Check if updatedAt is updated
//        assertNotEquals(originalUpdatedAt, session1.getUpdatedAt(), "The updatedAt field should be updated.");
//    }

}
