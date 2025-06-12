package com.capgemini.hospital_management_system.controller;


import com.capgemini.hospital_management_system.dto.PhysicianTrainedInDTO;
import com.capgemini.hospital_management_system.dto.ProcedureTrainedInDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.dto.TrainedInDTO;
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

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    
    //adding trained in data
    @PostMapping
	public ResponseEntity<Response<TrainedInDTO>> addTrainedIn(@RequestBody TrainedInDTO req) {
	    Physician physician = physicianRepository.findById(req.getPhysicianId())
	            .orElseThrow(() -> new RuntimeException("Physician not found"));

	    Procedure procedure = procedureRepository.findById(req.getProcedureId())
	            .orElseThrow(() -> new RuntimeException("Procedure not found"));

	    TrainedIn trainedIn = trainedInMapping.toEntity(req);
	    trainedIn.setPhysician(physician);
	    trainedIn.setTreatment(procedure);

	    TrainedIn saved = trainedInRepository.save(trainedIn);
	    TrainedInDTO responseDto = trainedInMapping.toDTO(saved);
	    return ResponseEntity.ok(new Response<>(200, "Record Created Successfully", responseDto,LocalDateTime.now()));
	}
	
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


}
