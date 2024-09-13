//package com.openclassrooms.starterjwt.security.jwt;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.ByteArrayOutputStream;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class AuthEntryJwtIntegrationTest {
//
//    @InjectMocks
//    private AuthEntryPointJwt authEntryPointJwt;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private AuthenticationException authException;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(authEntryPointJwt).build();
//    }
//
//    @Test
//    public void testCommence_UnauthorizedResponse() throws Exception {
//        when(request.getServletPath()).thenReturn("/test-path");
//        when(authException.getMessage()).thenReturn("Test unauthorized message");
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PrintWriter writer = new PrintWriter(outputStream);
//        when(response.getWriter()).thenReturn(writer);
//
//        authEntryPointJwt.commence(request, response, authException);
//        writer.flush();
//
//        String content = outputStream.toString();
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> actualBody = mapper.readValue(content, Map.class);
//
//        Map<String, Object> expectedBody = new HashMap<>();
//        expectedBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);
//        expectedBody.put("error", "Unauthorized");
//        expectedBody.put("message", "Test unauthorized message");
//        expectedBody.put("path", "/test-path");
//
//        assertEquals(expectedBody, actualBody);
//        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
//        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        outputStream.close();
//    }
//}