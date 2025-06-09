package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.PhysicianDto;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhysicianController.class)
public class PhysicianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhysicianRepository physicianRepository;

    @MockitoBean
    private org.modelmapper.ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // No special setup needed, handled by annotations
    }

    @Test
    void testGetPhysicianByName() throws Exception {
        Physician physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("John Doe");
        physician.setPosition("Cardiologist");
        physician.setSsn(12345);

        PhysicianDto physicianDto = new PhysicianDto(1, "John Doe", "Cardiologist", 12345);

        when(physicianRepository.findByName("John Doe")).thenReturn(Optional.of(physician));
        when(modelMapper.map(physician, PhysicianDto.class)).thenReturn(physicianDto);

        mockMvc.perform(get("/api/physician/name/John Doe"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.status").value(302))
                .andExpect(jsonPath("$.message").value("Physician found successfully!"))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

}
