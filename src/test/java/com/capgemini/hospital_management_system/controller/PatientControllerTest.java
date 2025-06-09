package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.PatientDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.mapper.PatientMapping;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import com.capgemini.hospital_management_system.repository.PatientRepository;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PhysicianRepository physicianRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientMapping patientMapping;
    @InjectMocks
    private PatientController patientController;

    private Patient patient;
    private PatientDTO patientDTO;
    private Physician physician;

    @BeforeEach
    void setUp() {
        physician = new Physician();
        physician.setEmployeeId(1);

        patient = new Patient();
        patient.setSsn(1001);
        patient.setPCP(physician);

        patientDTO = new PatientDTO();
        patientDTO.setSsn(1001);
    }

    @Test
    void getAllPatients_Success() {
        // Arrange
        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findAll()).thenReturn(patients);
        when(patientMapping.toDTO(any(Patient.class))).thenReturn(patientDTO);

        // Act
        ResponseEntity<Response<List<PatientDTO>>> responseEntity = patientController.getAllPatients();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Response<List<PatientDTO>> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Patients retrieved successfully", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals(patientDTO, response.getData().get(0));
        verify(patientRepository).findAll();
        verify(patientMapping).toDTO(patient);
    }

    @Test
    void getPatientsByPhysician_Success() {

        Integer physicianId = 1;
        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findByPCP_employeeId(physicianId)).thenReturn(Optional.of(patients));
        when(patientRepository.findAll()).thenReturn(patients);
        when(patientMapping.toDTO(any(Patient.class))).thenReturn(patientDTO);

        ResponseEntity<Response<List<PatientDTO>>> responseEntity =
                patientController.getPatientsByPhysician(physicianId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Response<List<PatientDTO>> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Patients for physician " + physicianId + " retrieved successfully",
                response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals(patientDTO, response.getData().get(0));
        verify(patientRepository).findByPCP_employeeId(physicianId);
        verify(patientRepository).findAll();
        verify(patientMapping).toDTO(patient);
    }

    @Test
    void getPatientByPhysicianAndId_Success() {
        // Arrange
        Integer physicianId = 1;
        Integer patientId = 100000001;
        when(patientRepository.findByPCP_EmployeeIdAndSsn(patientId, physicianId))
                .thenReturn(Optional.of(patient));
        when(patientMapping.toDTO(any(Patient.class))).thenReturn(patientDTO);

        ResponseEntity<Response<PatientDTO>> responseEntity =
                patientController.getPatientByPhysicianAndId(physicianId, patientId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Response<PatientDTO> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Patient retrieved successfully", response.getMessage());
        assertEquals(patientDTO, response.getData());
        verify(patientRepository).findByPCP_EmployeeIdAndSsn(patientId, physicianId);
        verify(patientMapping).toDTO(patient);
    }

}