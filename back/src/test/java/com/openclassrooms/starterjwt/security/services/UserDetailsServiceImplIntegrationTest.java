package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserDetailsServiceImplIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Prepare a test user with a unique email and save it to the repository
        testUser = new User();
        String uniqueEmail = "paul" + Math.random() + "@smith.com"; // Generates a unique email for each test run
        testUser.setEmail(uniqueEmail);
        testUser.setLastName("Paul");
        testUser.setFirstName("Smith");
        testUser.setPassword("password123");
        testUser.setAdmin(false);

        userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        // Clean up the repository after each test
        userRepository.deleteAll();
    }

    @Test
    public void testLoadUserByUsername_Success() {
        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getEmail());

        // Assert: Validate the user details
        assertNotNull(userDetails);
        assertEquals(testUser.getEmail(), userDetails.getUsername());
        assertEquals(testUser.getPassword(), userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("unknown@example.com"));
    }
}
