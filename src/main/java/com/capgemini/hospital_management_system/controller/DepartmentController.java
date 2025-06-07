package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.DepartmentDto;
import com.capgemini.hospital_management_system.dto.PhysicianDepartmentDto;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.model.Department;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.DepartmentRepository;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private ModelMapper modelMapper;


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



}
