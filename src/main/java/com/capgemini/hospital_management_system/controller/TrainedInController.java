package com.capgemini.hospital_management_system.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.hospital_management_system.dto.ProcedureDto;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.dto.TrainedInDto;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.ProcedureMapper;
import com.capgemini.hospital_management_system.mapper.TrainedInMapper;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.model.Procedure;
import com.capgemini.hospital_management_system.model.TrainedIn;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import com.capgemini.hospital_management_system.repository.ProceduresRepository;
import com.capgemini.hospital_management_system.repository.TrainedInRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TrainedInController {
	private final TrainedInRepository trainedInRepository;
	private final PhysicianRepository physicianRepository;
	private final ProceduresRepository procedureRepository;
	private final TrainedInMapper trainedInMapper;
	
	@PostMapping("/trained_in")
	public ResponseEntity<Response<TrainedInDto>> addCertificate(@RequestBody TrainedInDto req) {
	    Physician physician = physicianRepository.findById(req.getPhysicianId())
	            .orElseThrow(() -> new RuntimeException("Physician not found"));

	    Procedure procedure = procedureRepository.findById(req.getTreatmentId())
	            .orElseThrow(() -> new RuntimeException("Procedure not found"));

	    TrainedIn trainedIn = trainedInMapper.toEntity(req);
	    trainedIn.setPhysician(physician);
	    trainedIn.setTreatment(procedure);

	    TrainedIn saved = trainedInRepository.save(trainedIn);
	    TrainedInDto responseDto = trainedInMapper.toDto(saved);
	    return ResponseEntity.ok(new Response<>(200, "Record Created Successfully", responseDto,LocalDateTime.now()));
	}
	





}
