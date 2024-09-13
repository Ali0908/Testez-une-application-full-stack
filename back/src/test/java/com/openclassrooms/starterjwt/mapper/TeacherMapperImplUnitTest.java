package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TeacherMapperImplUnitTest {

    private TeacherMapperImpl teacherMapper;

    @BeforeEach
    public void setUp() {
        teacherMapper = new TeacherMapperImpl();
    }

    @Test
    public void testToEntity() {
        // Arrange: Create a TeacherDto
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        // Act: Convert to Teacher entity
        Teacher entity = teacherMapper.toEntity(dto);

        // Assert: Verify the conversion
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    public void testToDto() {
        // Arrange: Create a Teacher entity
        Teacher entity = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        // Act: Convert to TeacherDto
        TeacherDto dto = teacherMapper.toDto(entity);

        // Assert: Verify the conversion
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    public void testToEntity_List() {
        // Arrange: Create a list of TeacherDto
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setLastName("Doe");
        dto1.setFirstName("John");
        dto1.setCreatedAt(LocalDateTime.now());
        dto1.setUpdatedAt(LocalDateTime.now());


        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setLastName("Smith");
        dto2.setFirstName("Jane");
        dto2.setCreatedAt(LocalDateTime.now());
        dto2.setUpdatedAt(LocalDateTime.now());

        List<TeacherDto> dtoList = Arrays.asList(dto1, dto2);

        // Act: Convert to list of Teacher entities
        List<Teacher> entityList = teacherMapper.toEntity(dtoList);

        // Assert: Verify the conversion
        assertNotNull(entityList);
        assertEquals(2, entityList.size());
        assertEquals(dto1.getId(), entityList.get(0).getId());
        assertEquals(dto2.getId(), entityList.get(1).getId());
    }

    @Test
    public void testToDto_List() {
        // Arrange: Create a list of Teacher entities
        Teacher entity1 = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Teacher entity2 = Teacher.builder()
                .id(2L)
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Teacher> entityList = Arrays.asList(entity1, entity2);

        // Act: Convert to list of TeacherDto
        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

        // Assert: Verify the conversion
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(entity1.getId(), dtoList.get(0).getId());
        assertEquals(entity2.getId(), dtoList.get(1).getId());
    }

    @Test
    public void testToEntity_WithNull() {
        // Act: Convert null to Teacher entity
        Teacher entity = teacherMapper.toEntity((TeacherDto) null);

        // Assert: Result should be null
        assertNull(entity);
    }

    @Test
    public void testToDto_WithNull() {
        // Act: Convert null to TeacherDto
        TeacherDto dto = teacherMapper.toDto((Teacher) null);

        // Assert: Result should be null
        assertNull(dto);
    }
}
