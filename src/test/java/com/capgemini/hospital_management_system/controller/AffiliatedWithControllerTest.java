package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.AffiliatedWithDto;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.model.AffiliatedWith;
import com.capgemini.hospital_management_system.model.Department;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.AffiliatedWithRepository;
import com.capgemini.hospital_management_system.repository.DepartmentRepository;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AffiliatedWithControllerTest {

    @Mock
    private AffiliatedWithRepository affiliatedWithRepository;

    @Mock
    private PhysicianRepository physicianRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private AffiliatedWithController affiliatedWithController;

    private AffiliatedWithDto affiliatedWithDto;
    private Physician physician;
    private Department department;

    @BeforeEach
    void setUp() {
        affiliatedWithDto = new AffiliatedWithDto();
        affiliatedWithDto.setPhysicianId(101);
        affiliatedWithDto.setDepartmentId(1);
        affiliatedWithDto.setPrimaryAffiliation(true);

        physician = new Physician();
        physician.setEmployeeId(101);
        physician.setName("Dr. House");

        department = new Department();
        department.setDepartmentId(1);
        department.setName("Cardiology");
    }

    @Test
    void testCreateAffiliated() {
        // Arrange
        when(physicianRepository.findById(101)).thenReturn(Optional.of(physician));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));

        // Act
        ResponseEntity<Response<String>> responseEntity = affiliatedWithController.createAffiliated(affiliatedWithDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Affiliation created successfully", responseEntity.getBody().getMessage());
        assertEquals("Affiliation between Physician and Department has been created.", responseEntity.getBody().getData());
    }

    @Test
    void testHasPrimaryAffiliation() {
        Integer physicianId = 1;

        // Mock the repository method
        when(affiliatedWithRepository.existsByPhysicianEmployeeIdAndPrimaryAffiliationTrue(physicianId))
                .thenReturn(true);

        // Call the controller method
        ResponseEntity<Response<Boolean>> responseEntity = affiliatedWithController.hasPrimaryAffiliation(physicianId);

        // Assert response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().getData());
        assertEquals("Primary affiliation status retrieved", responseEntity.getBody().getMessage());
    }




}
