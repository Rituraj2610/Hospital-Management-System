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

}
