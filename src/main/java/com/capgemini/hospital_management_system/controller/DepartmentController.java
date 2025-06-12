package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.*;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.AffiliationPhysicianMapping;
import com.capgemini.hospital_management_system.mapper.PhysicianMapper;
import com.capgemini.hospital_management_system.model.Department;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.DepartmentRepository;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/department")
@Transactional
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PhysicianMapper physicianMapper;

    @Autowired
    private AffiliationPhysicianMapping affiliationPhysicianMapping;

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody CreateDepartmentDto request) {

        Department department = new Department();
        department.setDepartmentId(request.getDeptId());
        department.setName(request.getName());
        if(request.getHead() != null) {
            Physician head = physicianMapper.toEntity(request.getHead());
            head.setDepartments(new HashSet<>());
            head.setAffiliations(new HashSet<>());
            department.setHead(head);
        }

        if(request.getAffiliatedPhysicians() != null) {
            for(AffiliatedPhysicianDto dto : request.getAffiliatedPhysicians()) {
                Physician physician = affiliationPhysicianMapping.mapToPhysician(dto);
                physician.setDepartments(new HashSet<>());
                physician.setAffiliations(new HashSet<>());
                department.addPhysicianAffiliation(physician, dto.getPrimaryAffiliation());
            }
        }

        departmentRepository.save(department);

//        DepartmentDto responseDto = modelMapper.map(savedDepartment , DepartmentDto.class);
//
//        PhysicianDepartmentDto physicianDto = modelMapper.map(request.getHead(), PhysicianDepartmentDto.class);
//        responseDto.setPhysicianDetail(physicianDto);

//        Response<String> response = Response.<DepartmentDto>builder()
//                .status(HttpStatus.CREATED.value())
//                .message("Department created successfully")
//                .data(responseDto)
//                .time(LocalDateTime.now())
//                .build();

        return new ResponseEntity<>("Department created successfully", HttpStatus.CREATED);
    }



    @GetMapping("/")
    public ResponseEntity<Response<List<DepartmentDto>>> getAllDepartment() {

        List<Department> departmentList = departmentRepository.findAll();

        if (departmentList.isEmpty()) {
            throw new EntityNotFoundException("No departments found.");
        }

        List<DepartmentDto> departmentDtoList = departmentList.stream()
                .map(department -> {
                    DepartmentDto dto = modelMapper.map(department, DepartmentDto.class);

                    Physician head = department.getHead();
                    if (head != null) {
                        PhysicianDepartmentDto headDto = modelMapper.map(head, PhysicianDepartmentDto.class);
                        dto.setPhysicianDetail(headDto);
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        Response<List<DepartmentDto>> response = Response.<List<DepartmentDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Department list fetched successfully")
                .data(departmentDtoList)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response , HttpStatus.FOUND);
    }



    @GetMapping("/{deptId}")
    public ResponseEntity<Response<DepartmentDto>> getDepartmentDetailsById(@PathVariable Integer deptId){

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department with id " + deptId + " not found"));

        Physician head = department.getHead();

        DepartmentDto departmentDto = modelMapper.map(department, DepartmentDto.class);
        PhysicianDepartmentDto physicianDepartmentDto = modelMapper.map(head , PhysicianDepartmentDto.class);

        departmentDto.setPhysicianDetail(physicianDepartmentDto);

        Response<DepartmentDto> response = Response.<DepartmentDto>builder()
                .status(HttpStatus.OK.value())
                .message("Department fetched successfully")
                .data(departmentDto)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response , HttpStatus.FOUND);

    }

    @GetMapping("/head/{deptId}")
    public ResponseEntity<Response<PhysicianDepartmentDto>> getDepartmentHeadDetailsById(@PathVariable Integer deptId){

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department with id " + deptId + " not found"));

        Physician head = department.getHead();

        PhysicianDepartmentDto physicianDepartmentDto = modelMapper.map(head , PhysicianDepartmentDto.class);

        Response<PhysicianDepartmentDto> response = Response.<PhysicianDepartmentDto>builder()
                .status(HttpStatus.OK.value())
                .message("Physician fetched successfully")
                .data(physicianDepartmentDto)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response , HttpStatus.FOUND);

    }

    @GetMapping("/deptHead/{headId}")
    public ResponseEntity<Response<List<DepartmentDto>>> getDepartmentsByHeadId(@PathVariable Integer headId) {

        List<Department> departments = departmentRepository.findAllByHeadEmployeeId(headId);

        if (departments.isEmpty()) {
            throw new EntityNotFoundException("No departments found for head ID " + headId);
        }

        List<DepartmentDto> departmentDtos = departments.stream()
                .map(dept -> {
                    DepartmentDto dto = modelMapper.map(dept, DepartmentDto.class);
                    PhysicianDepartmentDto physicianDto = modelMapper.map(dept.getHead(), PhysicianDepartmentDto.class);
                    dto.setPhysicianDetail(physicianDto);
                    return dto;
                })
                .toList();

        Response<List<DepartmentDto>> response = Response.<List<DepartmentDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Departments fetched successfully for head ID " + headId)
                .data(departmentDtos)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/check/{physicianId}")
    public ResponseEntity<Boolean> checkHeadRole(@PathVariable Integer physicianId) {
        boolean isHead = departmentRepository.existsByHeadEmployeeId(physicianId);
        return new ResponseEntity<>(isHead , HttpStatus.OK);
    }

    @PutMapping("/update/headid/{deptId}")
    public ResponseEntity<Response<DepartmentDto>> updateDepartmentHead(
            @PathVariable Integer deptId,
            @RequestBody UpdateDepartmentHeadRequest request) {

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + deptId));

        Physician newHead = physicianRepository.findById(request.getPhysicianId())
                .orElseThrow(() -> new EntityNotFoundException("Physician not found with id: " + request.getPhysicianId()));

        department.setHead(newHead);
        departmentRepository.save(department);

        DepartmentDto departmentDto = modelMapper.map(department, DepartmentDto.class);
        PhysicianDepartmentDto physicianDto = modelMapper.map(newHead, PhysicianDepartmentDto.class);

        departmentDto.setPhysicianDetail(physicianDto);

        Response<DepartmentDto> response = Response.<DepartmentDto>builder()
                .status(HttpStatus.OK.value())
                .message("Department head updated successfully")
                .data(departmentDto)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/deptname/{deptid}")
    public ResponseEntity<Response<DepartmentDto>> updateDepartmentName(
            @PathVariable Integer deptid,
            @RequestParam String newName) {

        Department department = departmentRepository.findById(deptid)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + deptid));

        department.setName(newName);
        departmentRepository.save(department);

        DepartmentDto departmentDto = modelMapper.map(department, DepartmentDto.class);
        PhysicianDepartmentDto physicianDepartmentDto = modelMapper.map(department.getHead() , PhysicianDepartmentDto.class);

        departmentDto.setPhysicianDetail(physicianDepartmentDto);

        Response<DepartmentDto> response = Response.<DepartmentDto>builder()
                .status(HttpStatus.OK.value())
                .message("Department name updated successfully")
                .data(departmentDto)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
