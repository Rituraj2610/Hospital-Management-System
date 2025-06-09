package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.ProcedureDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.model.Procedure;
import com.capgemini.hospital_management_system.repository.ProceduresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcedureControllerTest {

    @InjectMocks
    private ProcedureController controller;

    @Mock
    private ProceduresRepository repository;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProcedureByIdFound() {
        Procedure procedure = new Procedure();
        procedure.setCode(101);
        procedure.setName("X-Ray");
        procedure.setCost(500.0);

        when(repository.findById(101)).thenReturn(Optional.of(procedure));

        ResponseEntity<Response<List<ProcedureDTO>>> response = controller.getProcedureById(101);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("X-Ray", response.getBody().getData().get(0).getName());
    }
}
