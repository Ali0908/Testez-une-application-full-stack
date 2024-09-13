//package com.openclassrooms.starterjwt.security;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test") // Use test profile for testing environment configuration
//public class WebSecurityConfigIntegrationTest {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"USER"})
//    public void testAuthenticatedAccess() throws Exception {
//        mockMvc.perform(get("/api/session"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testUnauthenticatedAccess() throws Exception {
//        mockMvc.perform(get("/api/session"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @WithMockUser(username = "marie@test.com", roles = {"USER"})
//    public void testAuthenticatedPostRequest() throws Exception {
//        mockMvc.perform(post("/api/session")
//                        .with(csrf())
//                        .contentType("application/json")
//                        .content("{\"key\":\"value\"}")) // Adjust content to match expected payload
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "marie@test.com", roles = {"USER"})
//    public void testAuthenticatedPutRequest() throws Exception {
//        mockMvc.perform(put("/api/session/1")
//                        .with(csrf())
//                        .contentType("application/json")
//                        .content("{\"key\":\"value\"}")) // Adjust content to match expected payload
//                .andExpect(status().isOk());
//    }
//}
