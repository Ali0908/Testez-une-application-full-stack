package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeacherEntityIntegrationTest {


    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void testConstructor_AllArgs() {
        // Arrange: Create a teacher using the all-args constructor
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher(1L, "Doe", "John", now, now);

        // Assert: Ensure that all fields are correctly set
        assertEquals(1L, teacher.getId());
        assertEquals("Doe", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    public void testSetters() {
        // Arrange: Create an empty teacher object
        Teacher teacher = new Teacher();

        // Act: Set the fields using the setter methods
        teacher.setLastName("Doe");
        teacher.setFirstName("John");
        LocalDateTime now = LocalDateTime.now();
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        // Assert: Check if the values are set correctly
        assertEquals("Doe", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    public void testEquals_SameId() {
        // Arrange: Create two teachers with the same id
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(1L, "Miller", "Jane", LocalDateTime.now(), LocalDateTime.now());

        // Assert: They should be equal since they have the same id
        assertEquals(teacher1, teacher2);
    }

    @Test
    public void testEquals_DifferentId() {
        // Arrange: Create two teachers with different ids
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(2L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        // Assert: They should not be equal since they have different ids
        assertNotEquals(teacher1, teacher2);
    }

    @Test
    public void testHashCode() {
        // Arrange: Create two teachers with the same id
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(1L, "Miller", "Jane", LocalDateTime.now(), LocalDateTime.now());

        // Assert: They should have the same hash code since they have the same id
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    public void testUpdatedAt_Setter() {
        // Arrange: Create a teacher
        Teacher teacher = new Teacher();
        LocalDateTime oldUpdatedAt = teacher.getUpdatedAt();

        // Act: Set a new updated at time
        LocalDateTime newUpdatedAt = LocalDateTime.now();
        teacher.setUpdatedAt(newUpdatedAt);

        // Assert: The updated at time should be updated
        assertNotEquals(oldUpdatedAt, teacher.getUpdatedAt());
        assertEquals(newUpdatedAt, teacher.getUpdatedAt());
    }


    @Test
    public void testTeacherBuilder_StringMethod() {
        // Arrange
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Act
        String teacherString = teacher.toString();

        // Assert
        // Assuming the string representation contains the id and last name
        assertTrue(teacherString.contains("1"));
        assertTrue(teacherString.contains("Doe"));
    }

    @Test
    public void testTeacherFieldValidation() {
        // Arrange: Create a validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Create a teacher with invalid data
        Teacher teacher = new Teacher();
        teacher.setLastName(""); // Invalid: NotBlank constraint
        teacher.setFirstName(""); // Invalid: NotBlank constraint

        // Act: Validate the teacher object
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Assert: There should be violations
        assertFalse(violations.isEmpty());
    }

//    @Test
//    public void testCreatedAt_Field() {
//        // Arrange: Create a teacher
//        Teacher teacher = new Teacher();
//        teacher.setLastName("Allen");
//        teacher.setFirstName("Quincy");
//
//        // Act: Save the teacher to the database
//        Teacher savedTeacher = teacherRepository.save(teacher);
//
//        // Assert: Ensure that createdAt is automatically set and not null
//        assertNotNull(savedTeacher.getCreatedAt());
//
//        // Attempt to set a new createdAt time (to check immutability)
//        LocalDateTime originalCreatedAt = savedTeacher.getCreatedAt();
//        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
//        savedTeacher.setCreatedAt(newTime);
//
//        // Save the teacher again
//        Teacher updatedTeacher = teacherRepository.save(savedTeacher);
//
//        // Assert: Ensure that createdAt remains unchanged after an attempt to modify it
//        assertEquals(originalCreatedAt, updatedTeacher.getCreatedAt());
//    }

    @Test
    public void testBuilder() {
        // Arrange: Use the builder pattern
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Assert: Ensure fields are set correctly
        assertEquals(1L, teacher.getId());
        assertEquals("Doe", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
    }


}