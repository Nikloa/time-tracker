package com.example.time_tracker.controller;

import com.example.time_tracker.model.dto.RecordDto;
import com.example.time_tracker.service.RecordService;
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

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class RecordControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RecordService service;

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        RecordDto recordDto = RecordDto.builder()
                .description("Description").startTime(new Date()).endTime(null).build();

        String requestBody = objectMapper.writeValueAsString(recordDto);

        mockMvc.perform(post("/api/user/records/new/project/1").contentType("application/json")
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {
        RecordDto recordDto = RecordDto.builder().id(1L)
                .description("Description").startTime(new Date()).endTime(new Date()).build();

        Mockito.doReturn(recordDto).when(service.createByProjectId(1L, recordDto));

        String requestBody = objectMapper.writeValueAsString(recordDto);

        mockMvc.perform(post("/api/user/records/new/project/1").contentType("application/json")
                .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());
        Mockito.verify(service, times(1)).createByProjectId(1L, recordDto);
    }

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        Long recordId = 123L;
        String requestURI = "/api/user/records/" + recordId;

        Mockito.when(service.findById(recordId)).thenThrow(ModelNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn200OK() throws Exception {
        Long recordId = 123L;
        String requestURI = "/api/user/records/" + recordId;
        String description = "Description";

        RecordDto recordDto = RecordDto.builder()
                .description(description).startTime(new Date()).endTime(new Date()).build();

        Mockito.when(service.findById(recordId)).thenReturn(recordDto);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.description").value(description))
                .andDo(print());
    }

    @Test
    public void testListShouldReturn200OK() throws Exception {
        RecordDto recordDto1 = RecordDto.builder().id(1L)
                .description("Description").startTime(new Date()).endTime(new Date()).build();
        RecordDto recordDto2 = RecordDto.builder().id(2L)
                .description("Description").startTime(new Date()).endTime(new Date()).build();

        List<RecordDto> list = List.of(recordDto1, recordDto2);

        Mockito.when(service.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/user/records"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].description").value(recordDto1.getDescription()))
                .andExpect(jsonPath("$[1].description").value(recordDto2.getDescription()))
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        Long recordId = 123L;
        String requestURI = "/api/user/records/" + recordId;

        RecordDto recordDto = RecordDto.builder().id(recordId)
                .description("Description").startTime(new Date()).endTime(new Date()).build();

        Mockito.when(service.updateById(recordId, recordDto)).thenThrow(ModelNotFoundException.class);

        String requestBody = objectMapper.writeValueAsString(recordDto);

        mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        Long recordId = 123L;
        String requestURI = "/api/user/records/" + recordId;
        String description = "Description";

        RecordDto recordDto = RecordDto.builder().id(recordId)
                .description(description).startTime(new Date()).endTime(new Date()).build();

        Mockito.when(service.updateById(recordId, recordDto)).thenReturn(recordDto);

        String requestBody = objectMapper.writeValueAsString(recordDto);

        mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {
        Long recordId = 123L;
        String requestURI = "/api/user/records/" + recordId;

        Mockito.doThrow(ModelNotFoundException.class).when(service).deleteById(recordId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn200OK() throws Exception {
        Long recordId = 123L;
        String requestURI = "/api/user/records/" + recordId;

        Mockito.doNothing().when(service).deleteById(recordId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
