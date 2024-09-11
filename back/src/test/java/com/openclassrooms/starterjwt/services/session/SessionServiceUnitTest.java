package com.openclassrooms.starterjwt.services.session;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class SessionServiceUnitTest {
    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testFindAll() {
        // Arrange: Mock the data
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Session 1");

        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Session 2");

        List<Session> mockSessions = Arrays.asList(session1, session2);

        // Mock the behavior of sessionRepository
        when(sessionRepository.findAll()).thenReturn(mockSessions);

        // Act: Call the service method
        List<Session> result = sessionService.findAll();

        // Assert: Check if the results match the mock data
        assertEquals(2, result.size());
        assertEquals("Session 1", result.get(0).getName());
        assertEquals("Session 2", result.get(1).getName());
    }


    @Test
    void testGetById() {
        // Arrange: Mock the data
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setName("Mock Session");

        // Mock the behavior of sessionRepository
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        // Act: Call the service method
        Session result = sessionService.getById(sessionId);

        // Assert: Check if the result matches the mock data
        assertEquals(sessionId, result.getId());
        assertEquals("Mock Session", result.getName());
    }

    @Test
    void testGetById_NotFound() {
        // Arrange: Mock the behavior for when session is not found
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act: Call the service method
        Session result = sessionService.getById(sessionId);

        // Assert: Result should be null when no session is found
        assertNull(result);
    }

    @Test
    void testUpdate() {
        // Arrange: Set up mock data
        Long sessionId = 1L;
        Session sessionToUpdate = new Session();
        sessionToUpdate.setName("Updated Session Name");

        // Mock the behavior of sessionRepository.save() to return the updated session
        Session updatedSession = new Session();
        updatedSession.setId(sessionId);
        updatedSession.setName("Updated Session Name");

        // When the sessionRepository.save() is called, return the updatedSession
        when(sessionRepository.save(sessionToUpdate)).thenReturn(updatedSession);

        // Act: Call the update method of SessionService
        Session result = sessionService.update(sessionId, sessionToUpdate);

        // Assert: Verify that the session's ID was set and the name is updated
        assertEquals(sessionId, result.getId());
        assertEquals("Updated Session Name", result.getName());
    }

    @Test
    void testDelete() {
        // Arrange: Define the session ID to be deleted
        Long sessionId = 1L;

        // Act: Call the delete method
        sessionService.delete(sessionId);

        // Assert: Verify that sessionRepository.deleteById() was called with the correct ID
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }
}
