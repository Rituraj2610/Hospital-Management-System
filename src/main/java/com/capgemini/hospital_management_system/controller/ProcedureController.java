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





}