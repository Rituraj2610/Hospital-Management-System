package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.*;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.model.Department;
import com.capgemini.hospital_management_system.model.Physician;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void testCreateDepartment_Success() {
        CreateDepartmentDto createDto = new CreateDepartmentDto();
        createDto.setDeptId(2);
        createDto.setName("Neurology");
        createDto.setPhysicianId(101);

        Department savedDepartment = new Department();
        savedDepartment.setDepartmentId(2);
        savedDepartment.setName("Neurology");
        savedDepartment.setHead(physician);

        DepartmentDto savedDto = new DepartmentDto();
        savedDto.setDepartmentId(2);
        savedDto.setName("Neurology");
        savedDto.setPhysicianDetail(physicianDto);

        when(physicianRepository.findById(101)).thenReturn(Optional.of(physician));
        when(departmentRepository.save(any(Department.class))).thenReturn(savedDepartment);
        when(modelMapper.map(savedDepartment, DepartmentDto.class)).thenReturn(savedDto);
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        ResponseEntity<Response<DepartmentDto>> response = departmentController.createDepartment(createDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Neurology", response.getBody().getData().getName());
        assertEquals(101, response.getBody().getData().getPhysicianDetail().getEmployeeId());
    }

    @Test
    void testGetAllDepartment_Success() {
        when(departmentRepository.findAll()).thenReturn(List.of(department));
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        ResponseEntity<Response<List<DepartmentDto>>> response = departmentController.getAllDepartment();

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertFalse(response.getBody().getData().isEmpty());
        assertEquals("Cardiology", response.getBody().getData().get(0).getName());
    }

    @Test
    void testGetAllDepartment_EmptyList_ThrowsException() {
        when(departmentRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> {
            departmentController.getAllDepartment();
        });
    }

    @Test
    void testGetDepartmentDetailsById_Success() {
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        ResponseEntity<Response<DepartmentDto>> response = departmentController.getDepartmentDetailsById(1);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(1, response.getBody().getData().getDepartmentId());
        assertEquals("Cardiology", response.getBody().getData().getName());
        assertEquals("Dr. House", response.getBody().getData().getPhysicianDetail().getName());
    }

    @Test
    void testGetDepartmentHeadDetailsById_Success() {
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        ResponseEntity<Response<PhysicianDepartmentDto>> response = departmentController.getDepartmentHeadDetailsById(1);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Dr. House", response.getBody().getData().getName());
        assertEquals(101, response.getBody().getData().getEmployeeId());
    }

    @Test
    void testGetDepartmentsByHeadId_Success() {
        when(departmentRepository.findAllByHeadEmployeeId(101)).thenReturn(List.of(department));
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        ResponseEntity<Response<List<DepartmentDto>>> response = departmentController.getDepartmentsByHeadId(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().getData().isEmpty());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testGetDepartmentsByHeadId_EmptyList_ThrowsException() {
        when(departmentRepository.findAllByHeadEmployeeId(999)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> {
            departmentController.getDepartmentsByHeadId(999);
        });
    }

    @Test
    void testCheckHeadRole_True() {
        when(departmentRepository.existsByHeadEmployeeId(101)).thenReturn(true);

        ResponseEntity<Boolean> response = departmentController.checkHeadRole(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testCheckHeadRole_False() {
        when(departmentRepository.existsByHeadEmployeeId(999)).thenReturn(false);

        ResponseEntity<Boolean> response = departmentController.checkHeadRole(999);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void testUpdateDepartmentHead_Success() {
        UpdateDepartmentHeadRequest request = new UpdateDepartmentHeadRequest();
        request.setPhysicianId(101);

        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(physicianRepository.findById(101)).thenReturn(Optional.of(physician));
        when(departmentRepository.save(department)).thenReturn(department);
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        ResponseEntity<Response<DepartmentDto>> response = departmentController.updateDepartmentHead(1, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(101, response.getBody().getData().getPhysicianDetail().getEmployeeId());
    }

    @Test
    void testUpdateDepartmentName_Success() {
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        ResponseEntity<Response<DepartmentDto>> response = departmentController.updateDepartmentName(1, "NewName");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cardiology", response.getBody().getData().getName());
    }
}
