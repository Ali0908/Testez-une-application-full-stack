package com.openclassrooms.starterjwt.services.teacher;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TeacherServiceIntegrationTest {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher firstTeacher;
    private Teacher secondTeacher;

    @BeforeEach
    void setUp() {

        // Create and save teachers
        firstTeacher = new Teacher();
        firstTeacher.setLastName("Smith");
        firstTeacher.setFirstName("John");
        firstTeacher.setCreatedAt(java.time.LocalDateTime.now().minusDays(1));
        teacherRepository.save(firstTeacher);

        secondTeacher = new Teacher();
        secondTeacher.setLastName("Doe");
        secondTeacher.setFirstName("Jane");
        secondTeacher.setCreatedAt(java.time.LocalDateTime.now().minusDays(1));
        teacherRepository.save(secondTeacher);
    }

    @Test
    void testFindAll() {
        // Act: Retrieve all teachers
        List<Teacher> teachers = teacherService.findAll();

        // Assert: Check if the list contains the teachers saved in the setup
        assertNotNull(teachers);
        assertTrue(teachers.contains(firstTeacher));
        assertTrue(teachers.contains(secondTeacher));
    }

    @Test
    void testFindByIdSuccess() {
        // Act: Retrieve a teacher by ID
        Teacher retrievedTeacher = teacherService.findById(firstTeacher.getId());

        // Assert: Check if the retrieved teacher matches the expected one
        assertNotNull(retrievedTeacher);
        assertEquals(firstTeacher.getId(), retrievedTeacher.getId());
        assertEquals(firstTeacher.getLastName(), retrievedTeacher.getLastName());
        assertEquals(firstTeacher.getFirstName(), retrievedTeacher.getFirstName());
    }

    @Test
    void testFindByIdNotFound() {
        // Act: Try to retrieve a teacher by a non-existent ID
        Teacher retrievedTeacher = teacherService.findById(999L);

        // Assert: Check if the result is null
        assertNull(retrievedTeacher);
    }
}
