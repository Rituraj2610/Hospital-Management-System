package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.*;
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
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AffiliatedWithControllerTest {

    @Mock
    private AffiliatedWithRepository affiliatedWithRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PhysicianRepository physicianRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private AffiliatedWithController affiliatedWithController; // Use consistent name

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
        when(physicianRepository.findById(101)).thenReturn(Optional.of(physician));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));

        ResponseEntity<Response<String>> responseEntity = affiliatedWithController.createAffiliated(affiliatedWithDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Affiliation created successfully", responseEntity.getBody().getMessage());
        assertEquals("Affiliation between Physician and Department has been created.", responseEntity.getBody().getData());
    }

    @Test
    void testHasPrimaryAffiliation() {
        Integer physicianId = 1;

        when(affiliatedWithRepository.existsByPhysicianEmployeeIdAndPrimaryAffiliationTrue(physicianId))
                .thenReturn(true);

        ResponseEntity<Response<Boolean>> responseEntity = affiliatedWithController.hasPrimaryAffiliation(physicianId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().getData());
        assertEquals("Primary affiliation status retrieved", responseEntity.getBody().getMessage());
    }

    @Test
    void testUpdatePrimaryAffiliation() {
        UpdatePrimaryAffiliationDto dto = new UpdatePrimaryAffiliationDto();
        dto.setDepartmentId(2);
        dto.setPrimaryAffiliation(true);

        Physician physician = new Physician();
        physician.setEmployeeId(1);

        Department department = new Department();
        department.setDepartmentId(2);

        AffiliatedWith affiliation = new AffiliatedWith();
        affiliation.setPhysician(physician);
        affiliation.setDepartment(department);
        affiliation.setPrimaryAffiliation(false);

        when(physicianRepository.findById(1)).thenReturn(Optional.of(physician));
        when(departmentRepository.findById(2)).thenReturn(Optional.of(department));
        when(affiliatedWithRepository.findByPhysicianEmployeeIdAndDepartmentDepartmentId(1, 2))
                .thenReturn(Optional.of(affiliation));
        when(affiliatedWithRepository.save(affiliation)).thenReturn(affiliation);

        ResponseEntity<Response<Boolean>> response = affiliatedWithController.updatePrimaryAffiliation(1, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData());
    }

    @Test
    void testGetPhysiciansByDepartment() {
        Integer deptId = 1;
        Physician physician = new Physician();
        physician.setEmployeeId(100);
        List<AffiliatedWith> affiliations = List.of(new AffiliatedWith());
        affiliations.get(0).setPhysician(physician);

        PhysicianDepartmentDto physicianDto = new PhysicianDepartmentDto();
        physicianDto.setEmployeeId(100);

        when(affiliatedWithRepository.findByDepartment_DepartmentId(deptId)).thenReturn(affiliations);
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        ResponseEntity<Response<List<PhysicianDepartmentDto>>> response = affiliatedWithController.getPhysiciansByDepartment(deptId);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertFalse(response.getBody().getData().isEmpty());
        assertEquals(100, response.getBody().getData().get(0).getEmployeeId());
    }

    @Test
    void testGetDepartmentsByPhysician() {
        Long physicianId = 10L;
        Department department = new Department();
        department.setDepartmentId(2);
        Physician physician = new Physician();
        physician.setEmployeeId(10);

        AffiliatedWith aff = new AffiliatedWith();
        aff.setDepartment(department);
        aff.setPhysician(physician);

        List<AffiliatedWith> affiliations = List.of(aff);

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentId(2);
        PhysicianDepartmentDto physicianDto = new PhysicianDepartmentDto();
        physicianDto.setEmployeeId(10);
        departmentDto.setPhysicianDetail(physicianDto);

        when(affiliatedWithRepository.findByPhysician_EmployeeId(physicianId)).thenReturn(affiliations);
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);
        when(modelMapper.map(physician, PhysicianDepartmentDto.class)).thenReturn(physicianDto);

        ResponseEntity<Response<List<DepartmentDto>>> response = affiliatedWithController.getDepartmentsByPhysician(physicianId);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertFalse(response.getBody().getData().isEmpty());
        assertEquals(2, response.getBody().getData().get(0).getDepartmentId());
    }

    @Test
    void testCountPhysiciansByDepartment() {
        Integer deptId = 1;
        List<AffiliatedWith> affiliations = List.of(new AffiliatedWith(), new AffiliatedWith());

        when(affiliatedWithRepository.findByDepartment_DepartmentId(deptId)).thenReturn(affiliations);

        ResponseEntity<Response<Integer>> response = affiliatedWithController.countPhysiciansByDepartment(deptId);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(2, response.getBody().getData());
    }
}
