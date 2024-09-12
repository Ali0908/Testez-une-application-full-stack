package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class TeacherControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher existingTeacher;
    private String jwtToken;

    @BeforeEach
    public void setUp() {
        // Authenticate and get JWT token
        this.jwtToken = authenticateAndGetToken();

        // Fetch the existing teacher with ID 1 from the database
        existingTeacher = teacherRepository.findById(1L).orElseThrow(() ->
                new IllegalStateException("Teacher with ID 1 not found in the database"));
    }

    private String authenticateAndGetToken() {
        // Create a login request with valid credentials
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("louis@test.com");  // Replace with valid email
        loginRequest.setPassword("password");    // Replace with valid password

        // Send login request and extract the JWT token
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        return response.getBody().getToken();
    }

    @Test
    public void testFindById_Success() {
        // Send a GET request to /api/teacher/{id} with the JWT token
        String url = "/api/teacher/" + existingTeacher.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        // Check the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testFindById_NotFound() {
        // Act: Perform a GET request for a non-existing teacher
        String url = "/api/teacher/999";
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
        String url = "/api/teacher/invalid-id";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        // Assert: Check if the response status is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAll_Success() {
        // URL to fetch all teachers
        String url = "/api/teacher/";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Add the JWT token to headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        // Assert: Check the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Assert: Check that the response contains at least one teacher
        List<?> teachers = response.getBody();
        assertFalse(teachers.isEmpty());
    }
}
