package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.CreateDepartmentDto;
import com.capgemini.hospital_management_system.dto.DepartmentDto;
import com.capgemini.hospital_management_system.dto.PhysicianDepartmentDto;
import com.capgemini.hospital_management_system.model.Department;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.repository.DepartmentRepository;

import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PhysicianRepository physicianRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DepartmentController departmentController;

    private Department department;
    private Physician physician;
    private DepartmentDto departmentDto;
    private PhysicianDepartmentDto physicianDto;

    @BeforeEach
    void setUp() {
        physician = new Physician();
        physician.setEmployeeId(101);
        physician.setName("Dr. House");

        department = new Department();
        department.setDepartmentId(1);
        department.setName("Cardiology");
        department.setHead(physician);

        departmentDto = new DepartmentDto();
        departmentDto.setDepartmentId(1);
        departmentDto.setName("Cardiology");

        physicianDto = new PhysicianDepartmentDto();
        physicianDto.setEmployeeId(101);
        physicianDto.setName("Dr. House");

        departmentDto.setPhysicianDetail(physicianDto);
    }

    @Test
    void testGetDepartmentDetailsById() {
        // Arrange
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        // Act
        ResponseEntity<Response<DepartmentDto>> responseEntity = departmentController.getDepartmentDetailsById(1);

        // Assert
        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getData().getDepartmentId());
        assertEquals("Cardiology", responseEntity.getBody().getData().getName());
        assertEquals("Dr. House", responseEntity.getBody().getData().getPhysicianDetail().getName());
    }

    @Test
    void testGetDepartmentHeadDetailsById() {
        // Arrange
        when(departmentRepository.findById(10)).thenReturn(Optional.of(department));
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        // Act
        ResponseEntity<Response<PhysicianDepartmentDto>> responseEntity =
                departmentController.getDepartmentHeadDetailsById(10);

        // Assert
        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Dr. House", responseEntity.getBody().getData().getName());
        assertEquals(101, responseEntity.getBody().getData().getEmployeeId());
    }


}
