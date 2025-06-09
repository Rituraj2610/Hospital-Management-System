package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.PhysicianDto;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/physician")
public class PhysicianController {

    @Autowired
    private PhysicianRepository physicianRepository;

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

}
