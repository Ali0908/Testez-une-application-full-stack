package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SessionServiceTest {
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
}
