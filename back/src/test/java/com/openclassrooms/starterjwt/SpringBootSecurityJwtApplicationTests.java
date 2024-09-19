package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.SpringApplication;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {
	@Autowired
	private UserRepository userRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testUserRepositoryIsLoaded() {
		assertNotNull(userRepository);
	}

//	@Test
//	public void testMainMethod() {
//		// Test if the SpringBootSecurityJwtApplication main method runs without exceptions
//		SpringBootSecurityJwtApplication.main(new String[]{});
//	}


}
