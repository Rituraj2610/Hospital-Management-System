package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.NurseDto;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.exception.EntityAlreadyExist;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.NurseCustomMapper;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.repository.NurseRepository;
import lombok.AllArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class NurseController {

    private final NurseRepository nurseRepository;
    private final NurseCustomMapper nurseCustomMapper;

    @PostMapping("/nurse")
    public ResponseEntity<Response<NurseDto>> addNurseDetails(@RequestBody NurseDto req) {
        Optional<Nurse> existingNurse = nurseRepository.findById(req.getEmployeeId());

        if (existingNurse.isPresent()) {
            throw new EntityAlreadyExist("Nurse with this ID " + req.getEmployeeId() + " already exists");
        }

        Nurse nurse = nurseCustomMapper.toEntity(req);
        nurseRepository.save(nurse);
        NurseDto responseDto = nurseCustomMapper.toDto(nurse);

        return ResponseEntity.ok(
            new Response<>(
                200,
                "Nurse saved successfully",
                responseDto,
                LocalDateTime.now()
            )
        );
    }

    @GetMapping("/nurse")
    public ResponseEntity<Response<List<NurseDto>>> getNurseDetails(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "2") Integer pageSize,
            @RequestParam(defaultValue = "employeeId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String keyword) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Nurse> nursePage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Check if keyword is numeric (for employeeId)
            if (keyword.matches("\\d+")) {
                int empId = Integer.parseInt(keyword);
                Optional<Nurse> optionalNurse = nurseRepository.findById(empId);
                List<Nurse> nurseList = optionalNurse.map(List::of).orElse(List.of());
                nursePage = new PageImpl<>(nurseList, pageable, nurseList.size());
            } else {
                // Search by name if not numeric
                nursePage = nurseRepository.findByNameContainingIgnoreCase(keyword, pageable);
            }
        } else {
            nursePage = nurseRepository.findAll(pageable);
        }

        if (nursePage.isEmpty()) {
            throw new EntityNotFoundException("Page is empty. Try another page or search again.");
        }

        List<NurseDto> dtoList = nurseCustomMapper.toDtoList(nursePage.getContent());

        return ResponseEntity.ok(new Response<>(
                200,
                "Nurses retrieved successfully",
                dtoList,
                LocalDateTime.now()
        ));
    }



    // Getting nurse by ID
    @GetMapping("/nurse/{empid}")
    public ResponseEntity<Response<NurseDto>> getNurseById(@PathVariable("empid") Integer id) {
        Optional<Nurse> nurse = nurseRepository.findById(id);

        if (nurse.isEmpty()) {

            throw new EntityNotFoundException("Nurse with employee ID " + id + " not found");
        }

        NurseDto responseDto = nurseCustomMapper.toDto(nurse.get());
        return ResponseEntity.ok(new Response<>(
            200,
            "Nurse with employee ID " + responseDto.getEmployeeId() + " retrieved successfully",
            responseDto,
            LocalDateTime.now()
        ));

    }

    @GetMapping("/nurse/position/{empid}")
    public ResponseEntity<Response<String>> getPositionById(@PathVariable("empid") Integer id) {
        Optional<Nurse> nurse = nurseRepository.findById(id);

        if (nurse.isEmpty()) {
            if (nurse.isEmpty()) {
                throw new EntityNotFoundException("Nurse with ID " + id + " does not exist");
            }
        }

        return ResponseEntity.ok(
            new Response<>(
                200,
                "Position retrieved successfully for employee ID " + id,
                nurse.get().getPosition(),
                LocalDateTime.now()
            )
        );
    }

    //accessing status for nurse by id

    @GetMapping("/nurse/registered/{empid}")
    public ResponseEntity<Response<Boolean>> isNurseRegistered(@PathVariable("empid") Integer id) {
        Optional<Nurse> nurse = nurseRepository.findById(id);

        if (nurse.isEmpty()) {
            throw new EntityNotFoundException("Nurse with ID " + id + " does not exist");
        }

        return ResponseEntity.ok(
            new Response<>(
                200,
                "Registered status retrieved successfully for employee ID " + id,
                nurse.get().getRegistered(),
                LocalDateTime.now()
            )
        );
    }

    @PutMapping("/nurse/registered/{empid}")
    public ResponseEntity<Response<Boolean>> updateRegisteredStatus(
            @PathVariable("empid") Integer id,
            @RequestBody Boolean registered) {

        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nurse with ID " + id + " does not exist"));

        nurse.setRegistered(registered);
        nurseRepository.save(nurse);

        return ResponseEntity.ok(new Response<>(
                200,
                "Registered status updated successfully for employee ID " + id,
                nurse.getRegistered(),
                LocalDateTime.now()
        ));
    }

    //updating ssn for nurse by id

    @PutMapping("/nurse/ssn/{empid}")
    public ResponseEntity<Response<NurseDto>> updateNurseSSN(
            @PathVariable("empid") Integer id,
            @RequestBody Integer newSsn) {

        Optional<Nurse> optionalNurse = nurseRepository.findById(id);
        if (optionalNurse.isEmpty()) {
            throw new EntityNotFoundException("Nurse with ID " + id + " does not exist");
        }

        Nurse nurse = optionalNurse.get();
        nurse.setSsn(newSsn);   // Update SSN
        nurseRepository.save(nurse);

        NurseDto responseDto = nurseCustomMapper.toDto(nurse);

        return ResponseEntity.ok(new Response<>(
                200,
                "SSN updated successfully for employee ID " + id,
                responseDto,
                LocalDateTime.now()
        ));
    }

    
    @PutMapping("/nurse/{id}")
    public ResponseEntity<Response<NurseDto>> partiallyUpdateNurse(
            @PathVariable("id") Integer id,
            @RequestBody NurseDto nurseDto) {

        Nurse existingNurse = nurseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nurse with ID " + id + " does not exist"));

        // ✅ Use a custom update method in your mapper
        nurseCustomMapper.updateNurseFromDto(nurseDto, existingNurse);

        nurseRepository.save(existingNurse);
        NurseDto updatedDto = nurseCustomMapper.toDto(existingNurse);

        return ResponseEntity.ok(new Response<>(
                200,
                "Nurse partially updated successfully",
                updatedDto,
                LocalDateTime.now()
        ));
    }

    



}
