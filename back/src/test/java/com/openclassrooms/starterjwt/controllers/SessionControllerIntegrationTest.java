package com.openclassrooms.starterjwt.controllers;


import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SessionControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;



    private Session existingSession;
    private Session updatingSession;
    private String jwtToken;


    @BeforeEach
    @Commit
    public void setUp() {

        // Create new users
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
        userRepository.save(userTest);


        UserDto secondUserDto = new UserDto();
        secondUserDto.setId(2L);
        secondUserDto.setEmail("benoit@test.com");
        secondUserDto.setPassword(passwordEncoder.encode("password"));
        secondUserDto.setFirstName("Benoit");
        secondUserDto.setLastName("Doe");
        secondUserDto.setAdmin(false);
        secondUserDto.setCreatedAt(LocalDateTime.now());
        secondUserDto.setUpdatedAt(LocalDateTime.now());
        userRepository.save(userMapper.toEntity(secondUserDto));

        UserDto thirdUserDto = new UserDto();
        thirdUserDto.setId(3L);
        thirdUserDto.setEmail("patrick@test.com");
        thirdUserDto.setPassword(passwordEncoder.encode("password"));
        thirdUserDto.setFirstName("Patrick");
        thirdUserDto.setLastName("Doe");
        thirdUserDto.setAdmin(false);
        thirdUserDto.setCreatedAt(LocalDateTime.now());
        thirdUserDto.setUpdatedAt(LocalDateTime.now());
        userRepository.save(userMapper.toEntity(thirdUserDto));

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("First Test Session");
        sessionDto.setDescription(" First Test Session Description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionRepository.save(sessionMapper.toEntity(sessionDto));

        SessionDto secondSessionDto = new SessionDto();
        secondSessionDto.setName("Second Test Session");
        secondSessionDto.setDescription(" Second Test Session Description");
        secondSessionDto.setDate(new Date());
        secondSessionDto.setTeacher_id(1L);
        sessionRepository.save(sessionMapper.toEntity(sessionDto));


        // Fetch the existing teacher with ID 1 from the database
        existingSession = sessionRepository.findById(1L).orElseThrow(() ->
                new IllegalStateException("Session with ID 1 not found in the database"));
        // Fetch the existing teacher with ID 1 from the database
        updatingSession = sessionRepository.findById(2L).orElseThrow(() ->
                new IllegalStateException("Session with ID 68 not found in the database"));
        this.jwtToken = authenticateAndGetToken();
    }

    private String authenticateAndGetToken() {
        // Create a login request with valid credentials
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("louis@test.com");  // Replace with a valid email
        loginRequest.setPassword("password");    // Replace with a valid password

        // Send login request and extract the JWT token
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        return response.getBody().getToken();
    }

    @Test
    public void testFindById_Success() {
        // Send a GET request to /api/session/{id} with the JWT token
        String url = "/api/session/" + existingSession.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<SessionDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, SessionDto.class);

        // Assert: Check the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(existingSession.getId(), response.getBody().getId());
        // Add other assertions based on your DTO fields
    }

    @Test
    public void testFindById_NotFound() {
        // Act: Perform a GET request for a non-existing session
        String url = "/api/session/999";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        // Assert: Check if the response status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindById_InvalidIdFormat() {
        // Send a GET request with an invalid ID format
        String url = "/api/session/invalid-id";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        // Assert: Check if the response status is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAll_Success() {
        // URL to fetch all sessions
        String url = "/api/session/";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        // Assert: Check the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Assert: Check that the response contains at least one teacher
        List<?> sessions = response.getBody();
        assertFalse(sessions.isEmpty());
    }

    @Test
    public void testCreate_Success() {
        // Prepare a new session DTO
        SessionDto newSessionDto = new SessionDto();
        newSessionDto.setName("Test session creation");
        newSessionDto.setDate(new Date()); // Use a valid date
        newSessionDto.setTeacher_id(1L); // Use an existing teacher ID
        newSessionDto.setDescription("Description of the new session");

        // URL for creating a new session
        String url = "/api/session";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SessionDto> entity = new HttpEntity<>(newSessionDto, headers);

        // Send POST request
        ResponseEntity<SessionDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, SessionDto.class);

        // Assert: Check the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdate_InvalidIdFormat() {
        // Prepare a modified session DTO with an invalid teacher ID
        SessionDto updateSessionDto = new SessionDto();
        updateSessionDto.setTeacher_id(1000L); // invalid teacher ID
        String url = "/api/session/" + existingSession.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SessionDto> entity = new HttpEntity<>(updateSessionDto, headers);

        // Send PUT request
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class);

        // Assert: Check the response status is BadRequest
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdate_Success() {
        // Transfer the existing session data to the DTO
        SessionDto updatingSessionDTO = sessionMapper.toDto(updatingSession);
        updatingSessionDTO.setName("Test session update");
        updatingSessionDTO.setDate(new Date());
        updatingSessionDTO.setTeacher_id(15L);
        updatingSessionDTO.setDescription("Updated description");

        String url = "/api/session/" + updatingSession.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SessionDto> entity = new HttpEntity<>(updatingSessionDTO, headers);

        // Send PUT request
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class);

        // Assert: Check the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testDelete_NotFound() {
        // URL to delete a non-existing session
        String url = "/api/session/999";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Send DELETE request
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Object.class);

        // Assert: Check the response status is NotFound
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testParticipate_Success() {
        // Arrange: Set up valid sessionId and userId
        Long validSessionId = existingSession.getId(); // Assuming this session exists
        // TODO: Check if the user exists in the database
        Long validUserId = 3L; // Assuming this user exists

        // URL for participate method
        String url = "/api/session/" + validSessionId + "/participate/" + validUserId;

        // Set headers and authentication token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Act: Send POST request to participate
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);

        // Assert: Verify the response status is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testParticipate_BadRequest() {
        // Arrange: Set up invalid sessionId and userId (non-numeric ID strings)
        String invalidSessionId = "invalid-session-id";
        String invalidUserId = "invalid-user-id";

        // URL for participate method with invalid IDs
        String url = "/api/session/" + invalidSessionId + "/participate/" + invalidUserId;

        // Set headers and authentication token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Act: Send POST request to participate
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);

        // Assert: Verify the response status is BAD_REQUEST (400)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipate_Success() {
        // Arrange: Set up valid sessionId and userId
        Long validSessionId = existingSession.getId();
        Long validUserId = 3L; // Assuming this user exists and was participating

        // URL for noLongerParticipate method
        String url = "/api/session/" + validSessionId + "/participate/" + validUserId;

        // Set headers with JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Act: Send DELETE request to noLongerParticipate
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Object.class);

        // Assert: Verify the response status is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void testNoLongerParticipate_NotFound() {
        // Arrange: Use a non-existing sessionId and userId
        Long nonExistingSessionId = 999L;
        Long nonExistingUserId = 999L;

        // URL for noLongerParticipate method with non-existing IDs
        String url = "/api/session/" + nonExistingSessionId + "/participate/" + nonExistingUserId;

        // Set headers with JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Act: Send DELETE request
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Object.class);

        // Assert: Verify the response status is NOT_FOUND (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
