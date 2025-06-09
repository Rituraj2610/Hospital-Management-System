package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.ProcedureDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.model.Procedure;
import com.capgemini.hospital_management_system.repository.ProceduresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/procedures")
public class ProcedureController {

    @Autowired
    private ProceduresRepository procedureRepository;

    @GetMapping
    public ResponseEntity<Response<List<ProcedureDTO>>> getAllProcedures() {
        List<ProcedureDTO> dtos = procedureRepository.findAll().stream()
                .map(p -> new ProcedureDTO(p.getCode(), p.getName(), p.getCost()))
                .collect(Collectors.toList());

        Response<List<ProcedureDTO>> response = Response.<List<ProcedureDTO>>builder()
                .status(200)
                .message("Success")
                .data(dtos)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // Get procedure by ID (code)
    @GetMapping("/{code}")
    public ResponseEntity<Response<List<ProcedureDTO>>> getProcedureById(@PathVariable Integer code) {
        Optional<Procedure> optionalProcedure = procedureRepository.findById(code);

        List<ProcedureDTO> dtos = optionalProcedure
                .map(p -> List.of(new ProcedureDTO(p.getCode(), p.getName(), p.getCost())))
                .orElse(Collections.emptyList());

        Response<List<ProcedureDTO>> response = Response.<List<ProcedureDTO>>builder()
                .status(dtos.isEmpty() ? 404 : 200)
                .message(dtos.isEmpty() ? "Procedure not found with code: " + code : "Success")
                .data(dtos)
                .build();

        return new ResponseEntity<>(response, dtos.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
    // Search procedures by name
    @GetMapping("/search")
    public ResponseEntity<Response<List<ProcedureDTO>>> searchProceduresByName(@RequestParam String name) {
        List<ProcedureDTO> filtered = procedureRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .map(p -> new ProcedureDTO(p.getCode(), p.getName(), p.getCost()))
                .collect(Collectors.toList());

        Response<List<ProcedureDTO>> response = Response.<List<ProcedureDTO>>builder()
                .status(filtered.isEmpty() ? 404 : 200)
                .message(filtered.isEmpty() ? "No procedures found matching: " + name : "Success")
                .data(filtered)
                .build();

        return new ResponseEntity<>(response, filtered.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createProcedure(@RequestBody ProcedureDTO dto) {
        // Convert DTO to entity
        Procedure procedure = new Procedure();
        procedure.setCode(dto.getCode());
        procedure.setName(dto.getName());
        procedure.setCost(dto.getCost());

        // Save to DB
        procedureRepository.save(procedure);

        return ResponseEntity.status(201).body("Record Created Successfully");
    }

    @PutMapping("/cost/{id}")
    public ResponseEntity<?> updateProcedureCost(
            @PathVariable Integer id,
            @RequestBody Map<String, Double> body) {

        // Extract cost value from request body
        Double newCost = body.get("cost");

        // Find the procedure by ID
        Optional<Procedure> optionalProcedure = procedureRepository.findById(id);
        if (optionalProcedure.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Update and save the new cost
        Procedure procedure = optionalProcedure.get();
        procedure.setCost(newCost);
        procedureRepository.save(procedure);

        return ResponseEntity.ok("Procedure cost updated successfully.");
    }
    @PutMapping("/name/{id}")
    public ResponseEntity<?> updateProcedureName(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        // Extract name from request body
        String newName = body.get("name");

        // Find the procedure by ID
        Optional<Procedure> optionalProcedure = procedureRepository.findById(id);
        if (optionalProcedure.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Update name and save
        Procedure procedure = optionalProcedure.get();
        procedure.setName(newName);
        procedureRepository.save(procedure);
        return ResponseEntity.ok("Procedure name updated successfully.");
    }





}