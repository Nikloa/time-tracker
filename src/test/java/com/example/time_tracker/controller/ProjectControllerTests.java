package com.example.time_tracker.controller;

import com.example.time_tracker.model.dto.ProjectDto;
import com.example.time_tracker.service.ProjectService;
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

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class ProjectControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProjectService service;

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        ProjectDto project = ProjectDto.builder().name("").build();

        String requestBody = objectMapper.writeValueAsString(project);

        mockMvc.perform(post("/api/projects/new").contentType("application/json")
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {
        ProjectDto projectDto = ProjectDto.builder().name("Test name").build();

        Mockito.when(service.create(projectDto)).thenReturn(projectDto);

        String requestBody = objectMapper.writeValueAsString(projectDto);

        mockMvc.perform(post("/api/projects/new").contentType("application/json")
                .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());
        Mockito.verify(service, times(1)).create(projectDto);
    }

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        Long projectId = 123L;
        String requestURI = "/api/projects/" + projectId;

        Mockito.when(service.findById(projectId)).thenThrow(ModelNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn200OK() throws Exception {
        Long projectId = 123L;
        String requestURI = "/api/projects/" + projectId;
        String name = "Test name";

        ProjectDto projectDto = ProjectDto.builder().id(projectId).name(name).build();

        Mockito.when(service.findById(projectId)).thenReturn(projectDto);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value(name))
                .andDo(print());
    }

    @Test
    public void testListShouldReturn200OK() throws Exception {
        ProjectDto projectDto1 = ProjectDto.builder().id(1L).name("Project1").build();
        ProjectDto projectDto2 = ProjectDto.builder().id(2L).name("Project2").build();

        List<ProjectDto> list = List.of(projectDto1, projectDto2);

        Mockito.when(service.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].name").value(projectDto1.getName()))
                .andExpect(jsonPath("$[1].name").value(projectDto2.getName()))
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        Long projectId = 123L;
        String requestURI = "/api/projects/" + projectId;

        ProjectDto projectDto = ProjectDto.builder().id(projectId).name("Project").build();

        Mockito.when(service.updateById(projectId, projectDto)).thenThrow(ModelNotFoundException.class);

        String requestBody = objectMapper.writeValueAsString(projectDto);

        mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        Long projectId = 123L;
        String requestURI = "/api/projects/" + projectId;
        String name = "Project";

        ProjectDto projectDto = ProjectDto.builder().id(projectId).name(name).build();

        Mockito.when(service.updateById(projectId, projectDto)).thenReturn(projectDto);

        String requestBody = objectMapper.writeValueAsString(projectDto);

        mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {
        Long projectId = 123L;
        String requestURI = "/api/projects/" + projectId;

        Mockito.doThrow(ModelNotFoundException.class).when(service).deleteById(projectId);;

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn200OK() throws Exception {
        Long projectId = 123L;
        String requestURI = "/api/projects/" + projectId;

        Mockito.doNothing().when(service).deleteById(projectId);;

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
