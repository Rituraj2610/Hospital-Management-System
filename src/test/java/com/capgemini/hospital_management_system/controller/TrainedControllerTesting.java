package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.ProcedureTrainedInDTO;
import com.capgemini.hospital_management_system.dto.TrainedInDTO;
import com.capgemini.hospital_management_system.mapper.PhysicianTrainedInMapping;
import com.capgemini.hospital_management_system.mapper.ProcedureTrainedInMapping;
import com.capgemini.hospital_management_system.mapper.TrainedInMapping;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.model.Procedure;
import com.capgemini.hospital_management_system.model.TrainedIn;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import com.capgemini.hospital_management_system.repository.ProceduresRepository;
import com.capgemini.hospital_management_system.repository.TrainedInRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainedInController.class)
public class TrainedControllerTesting {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainedInRepository trainedInRepository;

    @MockBean
    private PhysicianRepository physicianRepository;

    @MockBean
    private ProceduresRepository procedureRepository;

    @MockBean
    private TrainedInMapping trainedInMapping;

    @MockBean
    private ProcedureTrainedInMapping procedureTrainedInMapping;

    @MockBean
    private PhysicianTrainedInMapping physicianTrainedInMapping;

    @Test
    public void testAddCertificate_Success() throws Exception {
        // Input DTO
        TrainedInDTO requestDto = new TrainedInDTO(
                1,  // physicianId
                101, // procedureId
                LocalDateTime.of(2024, 6, 1, 10, 0),
                LocalDateTime.of(2025, 6, 1, 10, 0)
        );

        // Mock Entities
        Physician mockPhysician = new Physician();
        mockPhysician.setEmployeeId(1);

        Procedure mockProcedure = new Procedure();
        mockProcedure.setCode(101);

        TrainedIn trainedInEntity = new TrainedIn();
        trainedInEntity.setPhysician(mockPhysician);
        trainedInEntity.setTreatment(mockProcedure);
        trainedInEntity.setCertificationDate(requestDto.getCertificationDate());
        trainedInEntity.setCertificationExpires(requestDto.getCertificationExpires());

        // Setup mock behavior
        when(physicianRepository.findById(1)).thenReturn(Optional.of(mockPhysician));
        when(procedureRepository.findById(101)).thenReturn(Optional.of(mockProcedure));
        when(trainedInMapping.toEntity(any(TrainedInDTO.class))).thenReturn(trainedInEntity);
        when(trainedInRepository.save(any(TrainedIn.class))).thenReturn(trainedInEntity);
        when(trainedInMapping.toDTO(any(TrainedIn.class))).thenReturn(requestDto);

        // Perform POST request
        mockMvc.perform(post("/api/trained_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Record Created Successfully"))
                .andExpect(jsonPath("$.data.physicianId").value(1))
                .andExpect(jsonPath("$.data.procedureId").value(101));
                
    }
    
    @Test
    public void testGetCertifiedProcedures_Success() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Procedure procedure = new Procedure();
        procedure.setCode(1);
        procedure.setName("X-Ray");

        TrainedIn trainedIn = new TrainedIn();
        trainedIn.setTreatment(procedure);
        trainedIn.setCertificationExpires(now.plusDays(10));

        ProcedureTrainedInDTO dto = new ProcedureTrainedInDTO();
        dto.setCode(1);
        dto.setName("X-Ray");

        List<TrainedIn> trainedInList = List.of(trainedIn);
        List<ProcedureTrainedInDTO> expectedDtoList = List.of(dto);

        // Mocking repository and mapper
        when(trainedInRepository.findAll()).thenReturn(trainedInList);
        when(procedureTrainedInMapping.toDTO(procedure)).thenReturn(dto);

        // When & Then
        mockMvc.perform(get("/api/trained_in")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Certified procedures fetched successfully"))
                .andExpect(jsonPath("$.data[0].code").value(1))
                .andExpect(jsonPath("$.data[0].name").value("X-Ray"));
                
    }
}
