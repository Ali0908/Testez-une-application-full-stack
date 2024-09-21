package com.openclassrooms.starterjwt.services.session;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SessionServiceIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionMapper sessionMapper;


    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        // Create and save a session
//        session = new Session();
//        session.setName("Test Session");
//        session.setDescription("This is a test session description.");  // Set a valid description
//        ZoneId defaultZoneId = ZoneId.systemDefault();
//        LocalDate localDate = LocalDate.now();
//        Date date =  Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
//        session.setDate(date);  // Set a valid date
//        session.setUsers(new ArrayList<>()); // Initialize the users set
//        session = sessionRepository.save(session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("First Test Session");
        sessionDto.setDescription(" First Test Session Description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        session = sessionRepository.save(sessionMapper.toEntity(sessionDto));

        // Create and save a user
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("louis@test.com");
        userDto.setPassword(passwordEncoder.encode("password"));  // Make sure password matches the encoded password
        userDto.setFirstName("Louis");
        userDto.setLastName("Doe");
        userDto.setAdmin(true);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());
        User userTest = userMapper.toEntity(userDto);
        user = userRepository.save(userTest);
    }

    @Test
    void testParticipateSuccess() {
        // Act: User participates in session
        sessionService.participate(session.getId(), user.getId());

        // Assert: Check if the user has been added to the session's users
        Session updatedSession = sessionRepository.findById(session.getId()).orElse(null);
        assertNotNull(updatedSession);
        assertEquals(1, updatedSession.getUsers().size());
        assertTrue(updatedSession.getUsers().contains(user));
    }

    @Test
    void testParticipateAlreadyParticipating() {
        // Act: First participation
        sessionService.participate(session.getId(), user.getId());

        // Act & Assert: Try to participate again, expect BadRequestException
        assertThrows(BadRequestException.class, () -> sessionService.participate(session.getId(), user.getId()));
    }

    @Test
    void testParticipateInvalidSessionOrUser() {
        // Assert: Invalid session ID
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(999L, user.getId()); // Non-existent session ID
        });

        // Assert: Invalid user ID
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), 999L); // Non-existent user ID
        });
    }

    @Test
    void testNoLongerParticipateSuccess() {
        // Act: User participates in the session
        sessionService.participate(session.getId(), user.getId());

        // Ensure the user is indeed participating before attempting to remove
        Session initialSession = sessionRepository.findById(session.getId()).orElse(null);
        assertNotNull(initialSession);
        assertTrue(initialSession.getUsers().contains(user));

        // Act: User no longer participates in session
        sessionService.noLongerParticipate(session.getId(), user.getId());

        // Assert: Check if the user has been removed from the session's users
        Session updatedSession = sessionRepository.findById(session.getId()).orElse(null);
        assertNotNull(updatedSession);
        assertEquals(0, updatedSession.getUsers().size()); // No users should remain
        assertFalse(updatedSession.getUsers().contains(user));
    }

    @Test
    void testNoLongerParticipateSessionNotFound() {
        // Arrange: Set a non-existent session ID
        Long nonExistentSessionId = 999L;

        // Act & Assert: Expect NotFoundException when trying to remove a user from a non-existent session
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(nonExistentSessionId, user.getId()));
    }

    @Test
    void testNoLongerParticipateUserNotParticipating() {
        // Act & Assert: Expect BadRequestException when trying to remove a user who is not participating
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));
    }

}
