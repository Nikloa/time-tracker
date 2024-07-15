package com.example.time_tracker;

import com.example.time_tracker.config.SecurityConfig;
import com.example.time_tracker.controller.ProjectController;
import com.example.time_tracker.repository.ProjectRepository;
import com.example.time_tracker.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest
//@ContextConfiguration(classes = {TimeTrackerApplication.class})
//@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class TimeTrackerApplicationTests {

	@Autowired
	ProjectController projectController;
	@Autowired
	WebApplicationContext webApplicationContext;
	@Autowired
	MockMvc mockMvc;

	@Test
	void contextLoads() {
		assertThat(projectController).isNotNull();
		assertThat(mockMvc).isNotNull();
	}

}
