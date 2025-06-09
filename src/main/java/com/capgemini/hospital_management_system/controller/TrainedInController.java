package com.capgemini.hospital_management_system.controller;


import com.capgemini.hospital_management_system.dto.PhysicianTrainedInDTO;
import com.capgemini.hospital_management_system.dto.ProcedureTrainedInDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.dto.TrainedInDTO;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.PhysicianTrainedInMapping;
import com.capgemini.hospital_management_system.mapper.ProcedureTrainedInMapping;
import com.capgemini.hospital_management_system.mapper.TrainedInMapping;
import com.capgemini.hospital_management_system.model.TrainedIn;
import com.capgemini.hospital_management_system.model.TrainedInId;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import com.capgemini.hospital_management_system.repository.ProceduresRepository;
import com.capgemini.hospital_management_system.repository.TrainedInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trained_in")
@Slf4j
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

}
