package com.example.time_tracker.controller;

import com.example.time_tracker.model.auth.RegistrationRequest;
import com.example.time_tracker.model.dto.ProjectDto;
import com.example.time_tracker.model.dto.UserDto;
import com.example.time_tracker.service.ProjectService;
import com.example.time_tracker.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService service;

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .username("").email("test@gmail.com")
                .password("password").passwordConfirm("password")
                .role("ROLE_USER").build();

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/signup").contentType("application/json")
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .username("Test").email("test@gmail.com")
                .password("password").passwordConfirm("password")
                .role("ROLE_USER").build();
        UserDto userDto = UserDto.builder().id(1L)
                .username("Test").email("test@gmail.com")
                .role("ROLE_USER").build();

        Mockito.when(service.create(request)).thenReturn(userDto);

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/signup").contentType("application/json")
                .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());
        Mockito.verify(service, times(1)).create(request);
    }
}
