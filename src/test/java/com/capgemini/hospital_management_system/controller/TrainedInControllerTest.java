package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.PhysicianTrainedInDTO;
import com.capgemini.hospital_management_system.dto.ProcedureTrainedInDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.dto.TrainedInDTO;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.PhysicianTrainedInMapping;
import com.capgemini.hospital_management_system.mapper.ProcedureTrainedInMapping;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.model.Procedure;
import com.capgemini.hospital_management_system.model.TrainedIn;
import com.capgemini.hospital_management_system.model.TrainedInId;
import com.capgemini.hospital_management_system.repository.TrainedInRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainedInControllerTest {

    @Mock
    private TrainedInRepository trainedInRepository;

    @Mock
    private PhysicianTrainedInMapping physicianTrainedInMapping;

    @Mock
    private ProcedureTrainedInMapping procedureTrainedInMapping;

    @InjectMocks
    private TrainedInController trainedInController;

    private TrainedIn trainedIn;
    private Physician physician;
    private Procedure procedure;
    private PhysicianTrainedInDTO physicianDTO;
    private ProcedureTrainedInDTO procedureDTO;
    private TrainedInDTO trainedInDTO;



    @BeforeEach
    void setUp() {
        physician = new Physician();
        physician.setEmployeeId(4);

        procedure = new Procedure();
        procedure.setCode(100);

        trainedIn = new TrainedIn();
        trainedIn.setPhysician(physician); // Set Physician object, not int
        trainedIn.setTreatment(procedure);
        trainedIn.setCertificationDate(LocalDateTime.now().minusMonths(6));
        trainedIn.setCertificationExpires(LocalDateTime.now().plusYears(1));

        physicianDTO = new PhysicianTrainedInDTO();
        physicianDTO.setEmployeeId(4);
    }

    @Test
    void getPhysiciansByProcedure_Success() {
        // Arrange
        int procedureId = 100;
        List<TrainedIn> trainedIns = Arrays.asList(trainedIn);
        when(trainedInRepository.findByTreatmentCode(procedureId)).thenReturn(trainedIns);
        when(physicianTrainedInMapping.toDTO(any(Physician.class))).thenReturn(physicianDTO);

        // Act
        ResponseEntity<Response<List<PhysicianTrainedInDTO>>> responseEntity =
                trainedInController.getPhysiciansByProcedure(procedureId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Response<List<PhysicianTrainedInDTO>> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Physicians retrieved successfully", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals(physicianDTO, response.getData().get(0));
        verify(trainedInRepository).findByTreatmentCode(procedureId);
        verify(physicianTrainedInMapping).toDTO(physician);
    }

    @Test
    void getExpiringCertifications_Success() {
        // Arrange
        int physicianId = 4;
        List<TrainedIn> trainedIns = Arrays.asList(trainedIn);
        when(trainedInRepository.findByPhysicianEmployeeIdAndCertificationExpiresBetween(
                eq(physicianId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(trainedIns);
        when(procedureTrainedInMapping.toDTO(any(Procedure.class))).thenReturn(procedureDTO);

        // Act
        ResponseEntity<Response<List<ProcedureTrainedInDTO>>> responseEntity =
                trainedInController.getExpiringCertifications(physicianId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Response<List<ProcedureTrainedInDTO>> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Expiring certifications retrieved successfully", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals(procedureDTO, response.getData().get(0));
        verify(trainedInRepository).findByPhysicianEmployeeIdAndCertificationExpiresBetween(
                eq(physicianId), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(procedureTrainedInMapping).toDTO(procedure);
    }


}