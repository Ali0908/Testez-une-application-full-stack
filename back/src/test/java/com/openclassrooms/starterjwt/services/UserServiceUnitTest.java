package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testFindById() {
        // Arrange: Mock the data
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setLastName("Dupond");
        mockUser.setFirstName("Jean");
        mockUser.setEmail("jean@dupond.com");
        mockUser.setPassword("password");
        mockUser.setAdmin(false);

        // Mock the behavior of userRepository
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act: Call the service method
        User result = userService.findById(userId);

        // Assert: Check if the result matches the mock data
        assertEquals(userId, result.getId());
        assertEquals("Dupond", result.getLastName());
        assertEquals("Jean", result.getFirstName());


        // Assert: Check if email is non-null and in correct format
        assertNotNull(result.getEmail());
        assertTrue(result.getEmail().contains("@"));
        assertTrue(result.getEmail().endsWith(".com"));

        // Assert: Check if password is non-null
        assertNotNull(result.getPassword());

        // Assert: Check if admin is non-null (boolean should not be null)
        assertNotNull(result.isAdmin());
    }

    @Test
    void testDelete() {
        // Arrange: Define the session ID to be deleted
        Long userId = 1L;

        // Act: Call the delete method
        userService.delete(userId);

        // Assert: Verify that userRepository.deleteById() was called with the correct ID
        verify(userRepository, times(1)).deleteById(userId);
    }
}
