package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.PhysicianDto;
import com.capgemini.hospital_management_system.dto.PhysicianGroupByPositionDto;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.projection.ProcedureTrainingCount;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/physician")
public class PhysicianController {

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/name/{name}")
    public ResponseEntity<Response<PhysicianDto>> getPhysicianByName(@PathVariable String name) {
        // Find Physician by name
        Physician physician = physicianRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Physician not found with name: " + name));

        // Map Entity → DTO
        PhysicianDto physicianDto = new PhysicianDto(
                physician.getEmployeeId(),
                physician.getName(),
                physician.getPosition(),
                physician.getSsn()
        );

        // Build Response
        Response<PhysicianDto> response = new Response<>(
                HttpStatus.FOUND.value(),
                "Physician found successfully!",
                physicianDto,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }
    
    // GET /physician/position/{pos}
    @GetMapping("/position/{pos}")
    public ResponseEntity<Response<List<PhysicianDto>>> getPhysiciansByPosition(@PathVariable String pos) {
        List<Physician> physicians = physicianRepository.findAllByPosition(pos);

        if (physicians.isEmpty()) {
            throw new EntityNotFoundException("No physicians found with position: " + pos);
        }

        List<PhysicianDto> physicianDtos = physicians.stream()
                .map(physician -> modelMapper.map(physician, PhysicianDto.class))
                .collect(Collectors.toList());

        Response<List<PhysicianDto>> response = Response.<List<PhysicianDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Physicians fetched successfully")
                .data(physicianDtos)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /physician/empid/{empid}
    @GetMapping("/empid/{empid}")
    public ResponseEntity<Response<PhysicianDto>> getPhysicianByEmpId(@PathVariable Integer empid) {
        Physician physician = physicianRepository.findById(empid)
                .orElseThrow(() -> new EntityNotFoundException("Physician not found with Employee ID: " + empid));

        PhysicianDto physicianDto = modelMapper.map(physician, PhysicianDto.class);

        Response<PhysicianDto> response = Response.<PhysicianDto>builder()
                .status(HttpStatus.FOUND.value())
                .message("Physician fetched successfully")
                .data(physicianDto)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    // POST /api/physician/post
    @PostMapping("/post")
    public ResponseEntity<Response<PhysicianDto>> addPhysician(@RequestBody PhysicianDto physicianDto) {
        Physician physician = modelMapper.map(physicianDto, Physician.class);
        Physician savedPhysician = physicianRepository.save(physician);

        PhysicianDto responseDto = modelMapper.map(savedPhysician, PhysicianDto.class);

        Response<PhysicianDto> response = Response.<PhysicianDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Physician created successfully")
                .data(responseDto)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /update/position/{position}/{employeeId}
    @PutMapping("/update/position/{position}/{employeeId}")
    public ResponseEntity<Response<PhysicianDto>> updatePhysicianPosition(
            @PathVariable String position,
            @PathVariable Integer employeeId) {

        Physician physician = physicianRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Physician not found with Employee ID: " + employeeId));

        physician.setPosition(position);
        Physician updatedPhysician = physicianRepository.save(physician);

        PhysicianDto physicianDto = modelMapper.map(updatedPhysician, PhysicianDto.class);

        Response<PhysicianDto> response = Response.<PhysicianDto>builder()
                .status(HttpStatus.OK.value())
                .message("Physician position updated successfully")
                .data(physicianDto)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PUT /api/physician/update/name/{empid}?newName=XYZ
    @PutMapping("/update/name/{empid}")
    public ResponseEntity<Response<PhysicianDto>> updatePhysicianName(
            @PathVariable Integer empid,
            @RequestParam String newName) {

        Physician physician = physicianRepository.findById(empid)
                .orElseThrow(() -> new EntityNotFoundException("Physician not found with Employee ID: " + empid));

        physician.setName(newName);
        Physician updatedPhysician = physicianRepository.save(physician);

        PhysicianDto physicianDto = modelMapper.map(updatedPhysician, PhysicianDto.class);

        Response<PhysicianDto> response = Response.<PhysicianDto>builder()
                .status(HttpStatus.OK.value())
                .message("Physician name updated successfully")
                .data(physicianDto)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PUT /api/physician/update/ssn/{empid}?newSsn=XXXX
    @PutMapping("/update/ssn/{empid}")
    public ResponseEntity<Response<PhysicianDto>> updatePhysicianSSN(
            @PathVariable Integer empid,
            @RequestParam Integer newSsn) {

        Physician physician = physicianRepository.findById(empid)
                .orElseThrow(() -> new EntityNotFoundException("Physician not found with Employee ID: " + empid));

        physician.setSsn(newSsn);
        Physician updatedPhysician = physicianRepository.save(physician);

        PhysicianDto physicianDto = modelMapper.map(updatedPhysician, PhysicianDto.class);

        Response<PhysicianDto> response = Response.<PhysicianDto>builder()
                .status(HttpStatus.OK.value())
                .message("Physician SSN updated successfully")
                .data(physicianDto)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/physician/group-by-position
    @GetMapping("/group-by-position")
    public ResponseEntity<Response<List<PhysicianGroupByPositionDto>>> getPhysicianNamesGroupedByPosition() {

        List<Object[]> data = physicianRepository.findPositionAndPhysicianNames();

        // Grouping in Java
        Map<String, List<String>> groupedMap = data.stream()
                .collect(Collectors.groupingBy(
                        row -> (String) row[0], // position
                        Collectors.mapping(row -> (String) row[1], Collectors.toList())
                ));

        // Convert to List<PositionGroupDto>
        List<PhysicianGroupByPositionDto> groupedList = groupedMap.entrySet().stream()
                .map(entry -> new PhysicianGroupByPositionDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        Response<List<PhysicianGroupByPositionDto>> response = Response.<List<PhysicianGroupByPositionDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Physicians grouped by position with names")
                .data(groupedList)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/trained-procedures")
    public ResponseEntity<List<Map<String, Object>>> getTrainedProcedureCounts() {
        List<ProcedureTrainingCount> result = physicianRepository.countTrainedProceduresByName();

        List<Map<String, Object>> response = result.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("procedureName", r.getProcedureName());
            map.put("physicianCount", r.getPhysicianCount());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
