package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperImplUnitTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    public void testToEntity() {
        // Arrange: Create a UserDto
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("test@example.com");
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setPassword("password");
        dto.setAdmin(true);
        dto.setCreatedAt(null);  // Adjust as needed
        dto.setUpdatedAt(null);  // Adjust as needed

        // Act: Convert to User entity
        User user = userMapper.toEntity(dto);

        // Assert: Verify the conversion
        assertNotNull(user);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.getFirstName(), user.getFirstName());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.isAdmin(), user.isAdmin());
        assertEquals(dto.getCreatedAt(), user.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), user.getUpdatedAt());
    }

    @Test
    public void testToDto() {
        // Arrange: Create a User entity
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(true)
                .createdAt(null)  // Adjust as needed
                .updatedAt(null)  // Adjust as needed
                .build();

        // Act: Convert to UserDto
        UserDto dto = userMapper.toDto(user);

        // Assert: Verify the conversion
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(user.isAdmin(), dto.isAdmin());
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    public void testToEntity_List() {
        // Arrange: Create a list of UserDto
        UserDto dto1 = new UserDto();
        dto1.setId(1L);
        dto1.setEmail("email1@example.com");
        dto1.setLastName("Doe");
        dto1.setFirstName("John");
        dto1.setPassword("password");
        dto1.setAdmin(true);
        dto1.setCreatedAt(null);  // Set to a default or specific date if necessary
        dto1.setUpdatedAt(null);  // Set to a default or specific date if necessary

        UserDto dto2 = new UserDto();
        dto2.setId(2L);
        dto2.setEmail("email2@example.com");
        dto2.setLastName("Smith");
        dto2.setFirstName("Jane");
        dto2.setPassword("password");
        dto2.setAdmin(false);
        dto2.setCreatedAt(null);  // Set to a default or specific date if necessary
        dto2.setUpdatedAt(null);  // Set to a default or specific date if necessary

        List<UserDto> dtoList = Arrays.asList(dto1, dto2);

        // Act: Convert to list of User entities
        List<User> userList = userMapper.toEntity(dtoList);

        // Assert: Verify the conversion
        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals(dto1.getId(), userList.get(0).getId());
        assertEquals(dto2.getId(), userList.get(1).getId());
        assertEquals(dto1.getEmail(), userList.get(0).getEmail());
        assertEquals(dto2.getEmail(), userList.get(1).getEmail());
        assertEquals(dto1.getLastName(), userList.get(0).getLastName());
        assertEquals(dto2.getLastName(), userList.get(1).getLastName());
        assertEquals(dto1.getFirstName(), userList.get(0).getFirstName());
        assertEquals(dto2.getFirstName(), userList.get(1).getFirstName());
        assertEquals(dto1.getPassword(), userList.get(0).getPassword());
        assertEquals(dto2.getPassword(), userList.get(1).getPassword());
        assertEquals(dto1.isAdmin(), userList.get(0).isAdmin());
        assertEquals(dto2.isAdmin(), userList.get(1).isAdmin());
        assertEquals(dto1.getCreatedAt(), userList.get(0).getCreatedAt());
        assertEquals(dto2.getCreatedAt(), userList.get(1).getCreatedAt());
        assertEquals(dto1.getUpdatedAt(), userList.get(0).getUpdatedAt());
        assertEquals(dto2.getUpdatedAt(), userList.get(1).getUpdatedAt());
    }


    @Test
    public void testToDto_List() {
        // Arrange: Create a list of User entities with all required fields
        User user1 = User.builder()
                .id(1L)
                .email("email1@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .admin(true)
                .createdAt(LocalDateTime.now())  // Set a specific date or mock if needed
                .updatedAt(LocalDateTime.now())  // Set a specific date or mock if needed
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("email2@example.com")
                .lastName("Smith")
                .firstName("Jane")
                .password("password")
                .admin(false)
                .createdAt(LocalDateTime.now())  // Set a specific date or mock if needed
                .updatedAt(LocalDateTime.now())  // Set a specific date or mock if needed
                .build();

        List<User> userList = Arrays.asList(user1, user2);

        // Act: Convert to list of UserDto
        List<UserDto> dtoList = userMapper.toDto(userList);

        // Assert: Verify the conversion
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(user1.getId(), dtoList.get(0).getId());
        assertEquals(user2.getId(), dtoList.get(1).getId());
        assertEquals(user1.getEmail(), dtoList.get(0).getEmail());
        assertEquals(user2.getEmail(), dtoList.get(1).getEmail());
        assertEquals(user1.getLastName(), dtoList.get(0).getLastName());
        assertEquals(user2.getLastName(), dtoList.get(1).getLastName());
        assertEquals(user1.getFirstName(), dtoList.get(0).getFirstName());
        assertEquals(user2.getFirstName(), dtoList.get(1).getFirstName());
        assertEquals(user1.getPassword(), dtoList.get(0).getPassword());
        assertEquals(user2.getPassword(), dtoList.get(1).getPassword());
        assertEquals(user1.isAdmin(), dtoList.get(0).isAdmin());
        assertEquals(user2.isAdmin(), dtoList.get(1).isAdmin());
        assertEquals(user1.getCreatedAt(), dtoList.get(0).getCreatedAt());
        assertEquals(user2.getCreatedAt(), dtoList.get(1).getCreatedAt());
        assertEquals(user1.getUpdatedAt(), dtoList.get(0).getUpdatedAt());
        assertEquals(user2.getUpdatedAt(), dtoList.get(1).getUpdatedAt());
    }


    @Test
    public void testToEntity_WithNullList() {
        // Act: Convert a null list
        List<User> userList = userMapper.toEntity((List<UserDto>) null);

        // Assert: Verify that result is null
        assertNull(userList);
    }

    @Test
    public void testToDto_WithNullList() {
        // Act: Convert a null list
        List<UserDto> dtoList = userMapper.toDto((List<User>) null);

        // Assert: Verify that result is null
        assertNull(dtoList);
    }
}
