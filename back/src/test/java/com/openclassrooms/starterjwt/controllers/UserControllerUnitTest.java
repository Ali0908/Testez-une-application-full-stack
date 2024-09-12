package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("john@doe.com");

        // Mocking UserDto
        UserDto mockUserDto = new UserDto();
        mockUserDto.setId(userId);
        mockUserDto.setEmail("john@doe.com");

        // Mocking service and mapper behavior
        when(userService.findById(userId)).thenReturn(mockUser);
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);  // Return the mocked UserDto

        // Act
        ResponseEntity<?> response = userController.findById(userId.toString());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(mockUserDto, response.getBody());  // Assert that the body is the expected DTO
        verify(userService).findById(userId); // Ensure service is called
    }

    @Test
    public void testFindById_UserNotFound() {
        // Arrange
        Long userId = 1L;

        when(userService.findById(userId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById(userId.toString());

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService).findById(userId); // Ensure service is called
    }


    @Test
    public void testFindById_InvalidIdFormat() {
        // Act
        ResponseEntity<?> response = userController.findById("invalid_id");

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testDelete_Success() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("john@doe.com");

        when(userService.findById(userId)).thenReturn(mockUser);

        // Mock authenticated user
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("john@doe.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        ResponseEntity<?> response = userController.save(userId.toString());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(userService).delete(userId); // Ensure delete is called
    }

    @Test
    public void testDelete_UserNotFound() {
        // Arrange
        Long userId = 1L;

        when(userService.findById(userId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.save(userId.toString());

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(userService, never()).delete(anyLong()); // Ensure delete is not called
    }

    @Test
    public void testDelete_Unauthorized() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("john@doe.com");

        when(userService.findById(userId)).thenReturn(mockUser);

        // Mock authenticated user with a different email
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("different@user.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        ResponseEntity<?> response = userController.save(userId.toString());

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        verify(userService, never()).delete(anyLong()); // Ensure delete is not called
    }

    @Test
    public void testDelete_InvalidIdFormat() {
        // Act
        ResponseEntity<?> response = userController.save("invalid_id");

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        verify(userService, never()).delete(anyLong()); // Ensure delete is not called
    }
}
