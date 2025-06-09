package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.ProcedureDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.model.Procedure;
import com.capgemini.hospital_management_system.repository.ProceduresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcedureControllerTest {

    @Mock
    private ProceduresRepository procedureRepository;

    @InjectMocks
    private ProcedureController procedureController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProcedures() {
        Procedure p1 = new Procedure(101, "X-Ray", 500.0);
        Procedure p2 = new Procedure(102, "MRI", 1500.0);

        when(procedureRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        ResponseEntity<Response<List<ProcedureDTO>>> response = procedureController.getAllProcedures();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getData().size());
        verify(procedureRepository, times(1)).findAll();
    }
    @Test
    void testGetProcedureByIdNotFound() {
        when(procedureRepository.findById(999)).thenReturn(Optional.empty());

        ResponseEntity<Response<List<ProcedureDTO>>> response = procedureController.getProcedureById(999);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Procedure not found with code: 999", response.getBody().getMessage());
        assertTrue(response.getBody().getData().isEmpty());
    }
    @Test
    void testSearchProceduresByName_found() {
        // Arrange
        Procedure p1 = new Procedure();
        p1.setCode(1);
        p1.setName("X-Ray");
        p1.setCost(100.0);

        Procedure p2 = new Procedure();
        p2.setCode(2);
        p2.setName("MRI Scan");
        p2.setCost(200.0);

        when(procedureRepository.findAll()).thenReturn(List.of(p1, p2));

        // Act
        ResponseEntity<Response<List<ProcedureDTO>>> responseEntity = procedureController.searchProceduresByName("x-ray");

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Response<List<ProcedureDTO>> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals("X-Ray", response.getData().get(0).getName());

        verify(procedureRepository, times(1)).findAll();
    }

    @Test
    void testSearchProceduresByName_notFound() {
        // Arrange
        Procedure p = new Procedure();
        p.setCode(1);
        p.setName("Ultrasound");
        p.setCost(150.0);

        when(procedureRepository.findAll()).thenReturn(List.of(p));

        // Act
        ResponseEntity<Response<List<ProcedureDTO>>> responseEntity = procedureController.searchProceduresByName("X-Ray");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Response<List<ProcedureDTO>> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        assertEquals("No procedures found matching: X-Ray", response.getMessage());
        assertTrue(response.getData().isEmpty());

        verify(procedureRepository, times(1)).findAll();
    }
    @Test
    void testCreateProcedure() {
        ProcedureDTO dto = new ProcedureDTO(103, "CT Scan", 2000.0);
        Procedure entity = new Procedure(103, "CT Scan", 2000.0);

        when(procedureRepository.save(any())).thenReturn(entity);

        ResponseEntity<String> response = procedureController.createProcedure(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Record Created Successfully", response.getBody());
        verify(procedureRepository, times(1)).save(any());
    }
    @Test
    void testUpdateProcedureCostFound() {
        Procedure p = new Procedure(101, "X-Ray", 500.0);
        when(procedureRepository.findById(101)).thenReturn(Optional.of(p));

        Map<String, Double> body = new HashMap<>();
        body.put("cost", 750.0);

        ResponseEntity<?> response = procedureController.updateProcedureCost(101, body);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Procedure cost updated successfully.", response.getBody());
    }
    @Test
    void testUpdateProcedureCostNotFound() {
        when(procedureRepository.findById(999)).thenReturn(Optional.empty());

        Map<String, Double> body = new HashMap<>();
        body.put("cost", 999.0);

        ResponseEntity<?> response = procedureController.updateProcedureCost(999, body);

        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    void testUpdateProcedureNameFound() {
        Procedure p = new Procedure(101, "X-Ray", 500.0);
        when(procedureRepository.findById(101)).thenReturn(Optional.of(p));

        Map<String, String> body = new HashMap<>();
        body.put("name", "New X-Ray");

        ResponseEntity<?> response = procedureController.updateProcedureName(101, body);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Procedure name updated successfully.", response.getBody());
    }

}
