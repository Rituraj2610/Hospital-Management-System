package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.NurseDto;
import com.capgemini.hospital_management_system.mapper.NurseMapper;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.repository.NurseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        when(nurseMapper.toEntity(any(NurseDto.class))).thenReturn(nurseEntity);
        when(nurseRepository.save(any(Nurse.class))).thenReturn(nurseEntity);
        when(nurseMapper.toDto(any(Nurse.class))).thenReturn(dto);

        mockMvc.perform(post("/api/nurse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Nurse saved successfully"))
                .andExpect(jsonPath("$.data.name").value("Jane Doe"));
    }

    @Test
    void testGetNurseDetails() throws Exception {
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setName("John Smith");

        NurseDto nurseDto = new NurseDto();
        nurseDto.setEmployeeId(1);
        nurseDto.setName("John Smith");

        List<Nurse> nurses = List.of(nurse);
        List<NurseDto> nurseDtos = List.of(nurseDto);

        when(nurseRepository.findAll()).thenReturn(nurses);
        when(nurseMapper.toDtoList(nurses)).thenReturn(nurseDtos);

        mockMvc.perform(get("/api/nurse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Nurses retrieved successfully"))
                .andExpect(jsonPath("$.data[0].name").value("John Smith"));
    }

    @Test
    void testGetNurseDetailsById() throws Exception {
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setName("John Smith");

        NurseDto nurseDto = new NurseDto();
        nurseDto.setEmployeeId(1);
        nurseDto.setName("John Smith");

        when(nurseRepository.findById(1)).thenReturn(Optional.of(nurse));
        when(nurseMapper.toDto(nurse)).thenReturn(nurseDto);

        mockMvc.perform(get("/api/nurse/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Nurse with employee ID 1 retrieved successfully"))
                .andExpect(jsonPath("$.data.name").value("John Smith"));
    }
    
    @Test
    void testGetPositionById() throws Exception {
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setName("John Smith");
        nurse.setPosition("Head Nurse");

        // Mocking the repository
        when(nurseRepository.findById(1)).thenReturn(Optional.of(nurse));

        mockMvc.perform(get("/api/nurse/position/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Position retrieved successfully for employee ID 1"))
                .andExpect(jsonPath("$.data").value("Head Nurse"));
    }

}
