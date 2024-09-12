package com.openclassrooms.starterjwt.services.userDetails;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.HashSet;

@SpringBootTest
@ActiveProfiles("test") // Use a separate profile if necessary
public class UserDetailsImplIntegrationTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        // Arrange: Build a test user using the builder pattern
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();
    }

    @Test
    public void testGetUsername() {
        // Act: Retrieve the username
        String username = userDetails.getUsername();

        // Assert: Verify that the username is correct
        assertEquals("testuser", username);
    }

    @Test
    public void testGetAuthorities() {
        // Act: Retrieve the authorities
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Assert: Verify that the authorities are empty
        assertNotNull(authorities);
        assertInstanceOf(HashSet.class, authorities);
        assertEquals(0, authorities.size()); // Assuming no roles are assigned
    }

    @Test
    public void testIsAccountNonExpired() {
        // Act: Check if the account is non-expired
        boolean isAccountNonExpired = userDetails.isAccountNonExpired();

        // Assert: Verify that the account is non-expired
        assertTrue(isAccountNonExpired);
    }

    @Test
    public void testIsAccountNonLocked() {
        // Act: Check if the account is non-locked
        boolean isAccountNonLocked = userDetails.isAccountNonLocked();

        // Assert: Verify that the account is non-locked
        assertTrue(isAccountNonLocked);
    }

    @Test
    public void testIsCredentialsNonExpired() {
        // Act: Check if the credentials are non-expired
        boolean isCredentialsNonExpired = userDetails.isCredentialsNonExpired();

        // Assert: Verify that the credentials are non-expired
        assertTrue(isCredentialsNonExpired);
    }

    @Test
    public void testIsEnabled() {
        // Act: Check if the user is enabled
        boolean isEnabled = userDetails.isEnabled();

        // Assert: Verify that the user is enabled
        assertTrue(isEnabled);
    }

    @Test
    public void testEquals_sameObject() {
        // Assert: The user should equal itself
        assertEquals(userDetails, userDetails);
    }

    @Test
    public void testEquals_differentObject() {
        // Arrange: Create a different user object
        UserDetailsImpl anotherUser = UserDetailsImpl.builder()
                .id(2L)
                .username("anotheruser")
                .firstName("Jane")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        // Assert: The users should not be equal
        assertNotEquals(userDetails, anotherUser);
    }

    @Test
    public void testEquals_sameId() {
        // Arrange: Create another user with the same ID but different attributes
        UserDetailsImpl sameIdUser = UserDetailsImpl.builder()
                .id(1L) // Same ID as userDetails
                .username("anotheruser")
                .firstName("Jane")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        // Assert: The users should be equal since they have the same ID
        assertEquals(userDetails, sameIdUser);
    }
}
