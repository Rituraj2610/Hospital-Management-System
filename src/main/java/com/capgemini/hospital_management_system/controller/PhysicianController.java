package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.PhysicianDto;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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
    
}
