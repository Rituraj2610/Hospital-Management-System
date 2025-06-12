package com.capgemini.hospital_management_system.controller;


import com.capgemini.hospital_management_system.dto.*;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.PhysicianTrainedInMapping;
import com.capgemini.hospital_management_system.mapper.ProcedureTrainedInMapping;
import com.capgemini.hospital_management_system.mapper.TrainedInMapping;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.model.Procedure;
import com.capgemini.hospital_management_system.model.TrainedIn;
import com.capgemini.hospital_management_system.model.TrainedInId;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import com.capgemini.hospital_management_system.repository.ProceduresRepository;
import com.capgemini.hospital_management_system.repository.TrainedInRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trained_in")
@Slf4j
@AllArgsConstructor
public class TrainedInController {

    @Autowired
    private TrainedInRepository trainedInRepository;
    @Autowired
    private PhysicianRepository physicianRepository;
    @Autowired
    private ProceduresRepository procedureRepository;
    @Autowired
    private TrainedInMapping trainedInMapping;
    @Autowired
    private ProcedureTrainedInMapping procedureTrainedInMapping;
    @Autowired
    private PhysicianTrainedInMapping physicianTrainedInMapping;

    // ADITI
    @GetMapping("/physicians/{procedureId}")
    public ResponseEntity<Response<List<PhysicianTrainedInDTO>>> getPhysiciansByProcedure(@PathVariable int procedureId) {
        List<TrainedIn> trainedIns = trainedInRepository.findByTreatmentCode(procedureId);
        if (trainedIns.isEmpty()) {
            throw new EntityNotFoundException("No physicians found for procedure ID " + procedureId);
        }
        List<PhysicianTrainedInDTO> physicianDTOs = trainedIns.stream()
                .map(trainedIn -> physicianTrainedInMapping.toDTO(trainedIn.getPhysician()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Response<>(
                HttpStatus.OK.value(),
                "Physicians retrieved successfully",
                physicianDTOs,
                LocalDateTime.now()
        ));
    }

    @GetMapping("/expiredsooncerti/{physicianId}")
    public ResponseEntity<Response<List<ProcedureTrainedInDTO>>> getExpiringCertifications(@PathVariable int physicianId) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMonths(1);
        List<TrainedIn> trainedIns = trainedInRepository.findByPhysicianEmployeeIdAndCertificationExpiresBetween(
                physicianId, start, end);
        if (trainedIns.isEmpty()) {
            throw new EntityNotFoundException("No certifications expiring within a month for physician ID " + physicianId);
        }
        List<ProcedureTrainedInDTO> procedureDTOs = trainedIns.stream()
                .map(trainedIn -> procedureTrainedInMapping.toDTO(trainedIn.getTreatment()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Response<>(
                HttpStatus.OK.value(),
                "Expiring certifications retrieved successfully",
                procedureDTOs,
                LocalDateTime.now()
        ));
    }

    @PutMapping("/certificationexpiry/{physicianId}&{procedureId}")
    public ResponseEntity<Response<Boolean>> updateCertificationExpiry(
            @PathVariable int physicianId,
            @PathVariable int procedureId,
            @RequestBody TrainedInDTO trainedInDTO) {
        TrainedInId id = new TrainedInId(physicianId, procedureId);
        TrainedIn trainedIn = trainedInRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Certification not found for physician ID " + physicianId + " and procedure ID " + procedureId));
        if (trainedInDTO.getCertificationExpires() != null) {
            trainedIn.setCertificationExpires(trainedInDTO.getCertificationExpires());
            trainedInRepository.save(trainedIn);
            return ResponseEntity.ok(new Response<>(
                    HttpStatus.OK.value(),
                    "Certification expiry updated successfully",
                    true,
                    LocalDateTime.now()
            ));
        } else {
            throw new EntityNotFoundException("Certification expiry date is required");
        }
    }
    
    // ASHU
    @GetMapping
    public ResponseEntity<Response<List<ProcedureTrainedInDTO>>> getCertifiedProcedures(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "5", required = false) Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        LocalDateTime now = LocalDateTime.now();

        Set<Integer> seenProcedureCodes = new HashSet<>();

        List<ProcedureTrainedInDTO> certifiedProcedures = trainedInRepository.findAll().stream()
            .filter(trained -> trained.getCertificationExpires() != null &&
                               trained.getCertificationExpires().isAfter(now) &&
                               trained.getTreatment() != null)
            .map(TrainedIn::getTreatment)
            .filter(procedure -> seenProcedureCodes.add(procedure.getCode()))
            .map(procedureTrainedInMapping::toDTO)
            .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), certifiedProcedures.size());
        List<ProcedureTrainedInDTO> pagedList = certifiedProcedures.subList(start, end);

        Response<List<ProcedureTrainedInDTO>> response = new Response<>(
            200,
            "Certified procedures fetched successfully",
            pagedList,
            now
        );

        return ResponseEntity.ok(response);
    }

 // Fetching treatment by physician ID with pagination
    @GetMapping("/treatment/{physicianId}")
    public ResponseEntity<Response<List<ProcedureTrainedInDTO>>> getTreatmentsByPhysician(
            @PathVariable Integer physicianId,
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "5", required = false) Integer pageSize) {

        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<TrainedIn> trainedInList = trainedInRepository.findByPhysicianEmployeeId(physicianId);
        List<ProcedureTrainedInDTO> allProcedures = new ArrayList<>();

        for (TrainedIn trainedIn : trainedInList) {
            if (trainedIn != null && trainedIn.getTreatment() != null) {
                ProcedureTrainedInDTO dto = procedureTrainedInMapping.toDTO(trainedIn.getTreatment());
                if (dto != null) {
                    allProcedures.add(dto);
                }
            }
        }

        // Manual pagination using subList
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allProcedures.size());
        List<ProcedureTrainedInDTO> pagedList = allProcedures.subList(start, end);

        Response<List<ProcedureTrainedInDTO>> response = new Response<>(
            200,
            "Procedures for physician " + physicianId + " fetched successfully",
            pagedList,
            now
        );

        return ResponseEntity.ok(response);
    }

    // RITURAJ
    @PostMapping
    @Transactional
    public ResponseEntity<Response<TrainedInPostDTO>> addTrainedIn(@RequestBody TrainedInPostDTO req) {

        if (req.getPhysician() == null || req.getProcedure() == null ||
                req.getCertificationDate() == null || req.getCertificationExpires() == null) {
            throw new IllegalArgumentException("Physician, Procedure, and certification dates are required");
        }

        if (req.getCertificationDate().isAfter(req.getCertificationExpires())) {
            throw new IllegalArgumentException("Certification date must be before expiration date");
        }

        Physician physician = physicianRepository.findById(req.getPhysician().getEmployeeId())
                .orElseGet(() -> {
                    Physician newPhysician = new Physician();
                    newPhysician.setEmployeeId(req.getPhysician().getEmployeeId());
                    newPhysician.setName(req.getPhysician().getName());
                    newPhysician.setPosition(req.getPhysician().getPosition());
                    newPhysician.setSsn(req.getPhysician().getSsn());
                    return newPhysician; // Persisted via cascading
                });
        physicianRepository.save(physician);

        Procedure procedure = procedureRepository.findById(req.getProcedure().getCode())
                .orElseGet(() -> {
                    Procedure newProcedure = new Procedure();
                    newProcedure.setCode(req.getProcedure().getCode());
                    newProcedure.setName(req.getProcedure().getName());
                    newProcedure.setCost(req.getProcedure().getCost());
                    return newProcedure; // Persisted via cascading
                });
        procedureRepository.save(procedure);
        TrainedInId id = new TrainedInId(physician.getEmployeeId(), procedure.getCode());
        if (trainedInRepository.existsById(id)) {
            throw new IllegalStateException("Training relationship already exists");
        }

        TrainedIn trainedIn = new TrainedIn();
        trainedIn.setId(id);
        trainedIn.setPhysician(physician);
        trainedIn.setTreatment(procedure);
        trainedIn.setCertificationDate(req.getCertificationDate());
        trainedIn.setCertificationExpires(req.getCertificationExpires());

        TrainedIn saved = trainedInRepository.save(trainedIn);

        return ResponseEntity.ok(new Response<>(200, "Record Created Successfully", req,LocalDateTime.now()));
    }

    @GetMapping("/dates")
    public ResponseEntity<Response<PageResponse<TrainedInPostDTO>>> getByDates(
            @PageableDefault(size = 2) Pageable pageable,
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        Page<TrainedIn> page = trainedInRepository.findByDates(startDate, endDate, pageable);

        if (page.isEmpty()) {
            throw new EntityNotFoundException("No certifications found!");
        }

        List<TrainedInPostDTO> dtoList = page.getContent().stream()
                .map(trainedInMapping::toPostDTO)
                .collect(Collectors.toList());

        PageResponse<TrainedInPostDTO> pageResponse = new PageResponse<>(
                dtoList,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );

        Response<PageResponse<TrainedInPostDTO>> response = Response.<PageResponse<TrainedInPostDTO>>builder()
                .status(200)
                .message("Data fetched successfully!")
                .data(pageResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{physicianId}/{procedureId}")
    public ResponseEntity<TrainedInUpdateDTO> getCertification(
            @PathVariable int physicianId,
            @PathVariable int procedureId) {
        TrainedInId id = new TrainedInId(physicianId, procedureId);
        TrainedIn trainedIn = trainedInRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certification not found for physician ID " + physicianId + " and procedure ID " + procedureId));
        Procedure procedure = trainedIn.getTreatment();
        TrainedInUpdateDTO dto = new TrainedInUpdateDTO();
        ProcedureDTO procedureDTO = new ProcedureDTO();
        procedureDTO.setCode(procedureId);
        procedureDTO.setName(procedure.getName());
        procedureDTO.setCost(procedure.getCost());
        dto.setProcedure(procedureDTO);
        dto.setCertificationDate(trainedIn.getCertificationDate());
        dto.setCertificationExpires(trainedIn.getCertificationExpires());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update/{physicianId}/{procedureId}")
    public ResponseEntity<Response<Boolean>> updateCertification(
            @PathVariable int physicianId,
            @PathVariable int procedureId,
            @RequestBody TrainedInUpdateDTO updateDTO) {

        Procedure procedure = procedureRepository.findById(procedureId)
                .orElseThrow(() -> new EntityNotFoundException("Procedure not found"));

        if (updateDTO.getProcedure().getCost() != null) {
            procedure.setCost(updateDTO.getProcedure().getCost());
            procedureRepository.save(procedure);
        }

        TrainedInId id = new TrainedInId(physicianId, procedureId);

        TrainedIn trainedIn = trainedInRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certification not found"));

        if (updateDTO.getCertificationExpires() != null) {
            trainedIn.setCertificationExpires(updateDTO.getCertificationExpires());
            trainedInRepository.save(trainedIn);
        }
        return ResponseEntity.ok(new Response<>(
                HttpStatus.OK.value(),
                "Certification and procedure updated successfully",
                true,
                LocalDateTime.now()
        ));
    }
}