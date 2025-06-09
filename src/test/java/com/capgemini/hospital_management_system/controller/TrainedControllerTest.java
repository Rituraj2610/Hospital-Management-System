package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.ProcedureDto;
import com.capgemini.hospital_management_system.dto.TrainedInDto;
import com.capgemini.hospital_management_system.mapper.ProcedureMapper;
import com.capgemini.hospital_management_system.mapper.TrainedInMapper;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.model.Procedure;
import com.capgemini.hospital_management_system.model.TrainedIn;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import com.capgemini.hospital_management_system.repository.ProceduresRepository;
import com.capgemini.hospital_management_system.repository.TrainedInRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainedInController.class)
public class TrainedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainedInRepository trainedInRepository;

    @MockBean
    private PhysicianRepository physicianRepository;

    @MockBean
    private ProceduresRepository procedureRepository;

    @MockBean
    private TrainedInMapper trainedInMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddCertificate_Success() throws Exception {
        TrainedInDto inputDto = new TrainedInDto(1, 1,
                LocalDateTime.of(2025, 6, 8, 10, 0),
                LocalDateTime.of(2026, 6, 8, 10, 0));

        Physician physician = new Physician();
        physician.setEmployeeId(1);

        Procedure procedure = new Procedure();
        procedure.setCode(1);

        TrainedIn entity = new TrainedIn();
        entity.setPhysician(physician);
        entity.setTreatment(procedure);
        entity.setCertificationDate(inputDto.getCertificationDate());
        entity.setCertificationExpires(inputDto.getCertificationExpires());

        when(physicianRepository.findById(1)).thenReturn(Optional.of(physician));
        when(procedureRepository.findById(1)).thenReturn(Optional.of(procedure));
        when(trainedInMapper.toEntity(any(TrainedInDto.class))).thenReturn(entity);
        when(trainedInRepository.save(any(TrainedIn.class))).thenReturn(entity);
        when(trainedInMapper.toDto(any(TrainedIn.class))).thenReturn(inputDto);

        mockMvc.perform(post("/api/trained_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Record Created Successfully"))
                .andExpect(jsonPath("$.data.physicianId").value(1))
                .andExpect(jsonPath("$.data.treatmentId").value(1));
    }
    
    @Test
    public void testGetCertifiedProcedures() throws Exception {
        Procedure procedure = new Procedure();
        procedure.setCode(1);
        procedure.setName("Reverse Rhinopodoplasty");
        procedure.setCost(1500.00);

        TrainedIn trainedIn = new TrainedIn();
        trainedIn.setTreatment(procedure);
        trainedIn.setCertificationExpires(LocalDateTime.now().plusDays(10));

        ProcedureDto dto = new ProcedureDto(1, "Reverse Rhinopodoplasty", 1500.00);

        when(trainedInRepository.findAll()).thenReturn(List.of(trainedIn));

        try (MockedStatic<ProcedureMapper> mockedStatic = Mockito.mockStatic(ProcedureMapper.class)) {
            mockedStatic.when(() -> ProcedureMapper.toDto(procedure)).thenReturn(dto);

            mockMvc.perform(get("/api/trained_in"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Certified procedures fetched successfully"))
                    .andExpect(jsonPath("$.data[0].code").value(1))
                    .andExpect(jsonPath("$.data[0].name").value("Reverse Rhinopodoplasty"));
        }
    }
    
    
    @Test
    public void testGetTreatmentsByPhysician() throws Exception {
        int physicianId = 1;

        Procedure procedure = new Procedure();
        procedure.setCode(2);
        procedure.setName("Obtuse Pyloric Recombobulation");
        procedure.setCost(3750.00);

        TrainedIn trainedIn = new TrainedIn();
        trainedIn.setTreatment(procedure);

        ProcedureDto dto = new ProcedureDto(2, "Obtuse Pyloric Recombobulation", 3750.00);

        when(trainedInRepository.findByPhysician_EmployeeId(physicianId)).thenReturn(List.of(trainedIn));

        try (MockedStatic<ProcedureMapper> mockedStatic = Mockito.mockStatic(ProcedureMapper.class)) {
            mockedStatic.when(() ->ProcedureMapper.toDto(procedure)).thenReturn(dto);

            mockMvc.perform(get("/api/trained_in/treatment/{physicianId}", physicianId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Procedures for physician " + physicianId + " fetched successfully"))
                    .andExpect(jsonPath("$.data[0].code").value(2))
                    .andExpect(jsonPath("$.data[0].name").value("Obtuse Pyloric Recombobulation"));
        }
    }

   
}
