package com.example.time_tracker.controller;

import com.example.time_tracker.model.auth.RegistrationRequest;
import com.example.time_tracker.model.dto.UserDto;
import com.example.time_tracker.service.UserService;
import com.example.time_tracker.util.exception.ModelNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService service;

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        Long userId = 123L;
        String requestURI = "/api/users/" + userId;

        Mockito.when(service.findById(userId)).thenThrow(ModelNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn200OK() throws Exception {
        Long userId = 123L;
        String requestURI = "/api/users/" + userId;
        String username = "Test name";

        UserDto userDto = UserDto.builder().id(userId)
                .username(username).email("smth@mail.com").role("ROLE_USER").build();

        Mockito.when(service.findById(userId)).thenReturn(userDto);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username").value(username))
                .andDo(print());
    }

    @Test
    public void testListShouldReturn200OK() throws Exception {
        UserDto userDto1 = UserDto.builder().id(1L)
                .username("Test1").email("test1@mail.com").role("ROLE_USER").build();
        UserDto userDto2 = UserDto.builder().id(2L)
                .username("Test2").email("test2@mail.com").role("ROLE_USER").build();

        List<UserDto> list = List.of(userDto1, userDto2);

        Mockito.when(service.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].username").value(userDto1.getUsername()))
                .andExpect(jsonPath("$[1].username").value(userDto2.getUsername()))
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        Long userId = 123L;
        String requestURI = "/api/users/" + userId;

        RegistrationRequest request = RegistrationRequest.builder()
                .username("Test").email("test@mail.com").password("password")
                .passwordConfirm("password").role("ROLE_USER").build();

        Mockito.when(service.updateById(userId, request)).thenThrow(ModelNotFoundException.class);

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        Long userId = 123L;
        String requestURI = "/api/users/" + userId;
        String username = "Tests";

        RegistrationRequest request = RegistrationRequest.builder()
                .username(username).email("tests@mail.com")
                .password("password").passwordConfirm("password")
                .role("ROLE_USER").build();
        UserDto userDto = UserDto.builder().id(userId)
                .username(username).email("tests@mail.com").role("ROLE_USER").build();

        Mockito.when(service.updateById(userId, request)).thenReturn(userDto);

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {
        Long userId = 123L;
        String requestURI = "/api/users/" + userId;

        Mockito.doThrow(ModelNotFoundException.class).when(service).deleteById(userId);;

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn200OK() throws Exception {
        Long userId = 123L;
        String requestURI = "/api/users/" + userId;

        Mockito.doNothing().when(service).deleteById(userId);;

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andDo(print());
    }
}

