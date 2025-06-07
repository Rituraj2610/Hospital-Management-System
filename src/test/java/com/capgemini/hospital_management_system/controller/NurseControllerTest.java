package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.NurseDto;
import com.capgemini.hospital_management_system.mapper.NurseMapper;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.repository.NurseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NurseController.class)
class NurseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NurseRepository nurseRepository;

    @MockBean
    private NurseMapper nurseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddNurseDetails() throws Exception {
        // Given
        NurseDto dto = new NurseDto();
        dto.setEmployeeId(1);
        dto.setName("Jane Doe");
        dto.setPosition("Senior Nurse");
        dto.setRegistered(true);
        dto.setSsn(123456);

        Nurse nurseEntity = new Nurse();
        nurseEntity.setEmployeeId(1);
        nurseEntity.setName("Jane Doe");
        nurseEntity.setPosition("Senior Nurse");
        nurseEntity.setRegistered(true);
        nurseEntity.setSsn(123456);

        // When
        Mockito.when(nurseMapper.toEntity(any(NurseDto.class))).thenReturn(nurseEntity);
        Mockito.when(nurseRepository.save(any(Nurse.class))).thenReturn(nurseEntity);
        Mockito.when(nurseMapper.toDto(any(Nurse.class))).thenReturn(dto);

        // Then
        mockMvc.perform(post("/api/nurse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Nurse saved successfully"))
                .andExpect(jsonPath("$.data.name").value("Jane Doe"));
    }
}
