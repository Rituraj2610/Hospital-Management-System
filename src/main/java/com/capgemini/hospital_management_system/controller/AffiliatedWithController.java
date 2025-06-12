package com.capgemini.hospital_management_system.controller;


import com.capgemini.hospital_management_system.dto.*;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.model.AffiliatedWith;
import com.capgemini.hospital_management_system.model.AffiliatedWithId;
import com.capgemini.hospital_management_system.model.Department;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.AffiliatedWithRepository;
import com.capgemini.hospital_management_system.repository.DepartmentRepository;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/affiliated_with")
@Transactional
public class AffiliatedWithController {

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AffiliatedWithRepository affiliatedWithRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<Response<List<ResponseAffiliatedDto>>> getAllAffiliatedWith() {
        List<AffiliatedWith> affiliatedWith = affiliatedWithRepository.findAll();
        List<ResponseAffiliatedDto> responseAffiliatedDtoList = new ArrayList<>();
        for(AffiliatedWith a : affiliatedWith){
            responseAffiliatedDtoList.add(new ResponseAffiliatedDto(a.getPhysician().getName(), a.getDepartment().getName(), a.getPrimaryAffiliation()));
        }
        Response<List<ResponseAffiliatedDto>> response = Response.<List<ResponseAffiliatedDto>>builder()
                .status(HttpStatus.OK.value())
                .message("All Physicians affiliated with department")
                .data(responseAffiliatedDtoList)
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/physicians/{deptId}")
    public ResponseEntity<Response<List<PhysicianDepartmentDto>>> getPhysiciansByDepartment(@PathVariable Integer deptId) {
        List<AffiliatedWith> affiliations = affiliatedWithRepository.findByDepartment_DepartmentId(deptId);

        List<PhysicianDepartmentDto> physicianDtos = affiliations.stream()
                .map(aff -> modelMapper.map(aff.getPhysician(), PhysicianDepartmentDto.class))
                .toList();

        Response<List<PhysicianDepartmentDto>> response = Response.<List<PhysicianDepartmentDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Physicians affiliated with department ID: " + deptId)
                .data(physicianDtos)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response , HttpStatus.FOUND);

    }



    @GetMapping("/department/{physicianId}")
    public ResponseEntity<Response<List<DepartmentDto>>> getDepartmentsByPhysician(@PathVariable Long physicianId) {
        List<AffiliatedWith> affiliations = affiliatedWithRepository.findByPhysician_EmployeeId(physicianId);

        List<DepartmentDto> departments = affiliations.stream()
                .map(aff -> {

                    DepartmentDto dto = modelMapper.map(aff.getDepartment(), DepartmentDto.class);

                    PhysicianDepartmentDto physicianDto = modelMapper.map(aff.getPhysician(), PhysicianDepartmentDto.class);
                    dto.setPhysicianDetail(physicianDto);

                    return dto;
                })
                .toList();

        Response<List<DepartmentDto>> response = Response.<List<DepartmentDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Departments affiliated with physician ID: " + physicianId)
                .data(departments)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response , HttpStatus.FOUND);
    }


    @GetMapping("/count-physicians/{deptId}")
        public ResponseEntity<Response<Integer>> countPhysiciansByDepartment(@PathVariable Integer deptId) {
            int count = affiliatedWithRepository.findByDepartment_DepartmentId(deptId).size();

            Response<Integer> response = Response.<Integer>builder()
                    .status(HttpStatus.OK.value())
                    .message("Physician count for department ID: " + deptId)
                    .data(count)
                    .time(LocalDateTime.now())
                    .build();

            return new ResponseEntity<>(response , HttpStatus.FOUND);
        }


    @PostMapping("/post")
    public ResponseEntity<Response<String>> createAffiliated(@RequestBody AffiliatedWithDto affiliatedWithDto) {

        Physician physician = physicianRepository.findById(affiliatedWithDto.getPhysicianId())
                .orElseThrow(() -> new EntityNotFoundException("Physician not found"));

        Department department = departmentRepository.findById(affiliatedWithDto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));

        AffiliatedWith affiliatedWith = new AffiliatedWith();

        affiliatedWith.setPhysician(physician);
        affiliatedWith.setDepartment(department);
        affiliatedWith.setPrimaryAffiliation(affiliatedWithDto.getPrimaryAffiliation());

        affiliatedWithRepository.save(affiliatedWith);

        Response<String> response = Response.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("Affiliation created successfully")
                .data("Affiliation between Physician and Department has been created.")
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/primary/{physicianId}")
    public ResponseEntity<Response<Boolean>> hasPrimaryAffiliation(@PathVariable Integer physicianId) {
        boolean hasPrimary = affiliatedWithRepository.existsByPhysicianEmployeeIdAndPrimaryAffiliationTrue(physicianId);

        Response<Boolean> response = Response.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Primary affiliation status retrieved")
                .data(hasPrimary)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/primary/{physicianId}")
    public ResponseEntity<Response<Boolean>> updatePrimaryAffiliation(
            @PathVariable Integer physicianId,
            @RequestBody UpdatePrimaryAffiliationDto dto) {

        Physician physician = physicianRepository.findById(physicianId)
                .orElseThrow(() -> new EntityNotFoundException("Physician not found"));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));

        AffiliatedWith affiliation = affiliatedWithRepository
                .findByPhysicianEmployeeIdAndDepartmentDepartmentId(physicianId, dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Affiliation not found"));

        if (Boolean.TRUE.equals(affiliation.getPrimaryAffiliation())
                && Boolean.TRUE.equals(dto.getPrimaryAffiliation())) {
            throw new IllegalStateException("This department is already marked as the primary affiliation.");
        }

        affiliation.setPrimaryAffiliation(dto.getPrimaryAffiliation());
        affiliatedWithRepository.save(affiliation);

        Response<Boolean> response = Response.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Primary affiliation updated successfully")
                .data(true)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PutMapping
//    public ResponseEntity<Response<Integer>> updateDepartment(@RequestBody AffiliatedWithDto affiliatedWithDto) {
//        AffiliatedWith affiliation = affiliatedWithRepository.findByPhysician_EmployeeId(affiliatedWithDto.getPhysicianId())
//                .orElseThrow(() -> new EntityNotFoundException("Affiliation not found for physicianId: " + affiliatedWithDto.getPhysicianId() ));
//        Physician physician = physicianRepository.findById(affiliatedWithDto.getPhysicianId())
//                .orElseThrow(() -> new EntityNotFoundException("Physician not found: " + affiliatedWithDto.getPhysicianId()));
//        Department department = departmentRepository.findById(affiliatedWithDto.getDepartmentId())
//                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + affiliatedWithDto.getDepartmentId()));
//        affiliation.setPhysician(physician);
//        affiliation.setDepartment(department);
//        if (!physician.getAffiliations().contains(affiliation)) {
//            physician.getAffiliations().add(affiliation);
//        }
//        if (!department.getAffiliations().contains(affiliation)) {
//            department.getAffiliations().add(affiliation);
//        }
//
//        // Save changes (cascades to Physician and Department via MERGE)
//        affiliatedWithRepository.save(affiliation);
//        Response<Integer> response = Response.<Integer>builder()
//                .status(HttpStatus.OK.value())
//                .message("Primary affiliation updated successfully")
//                .data(department.getDepartmentId())
//                .time(LocalDateTime.now())
//                .build();
//        System.out.println();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

}
