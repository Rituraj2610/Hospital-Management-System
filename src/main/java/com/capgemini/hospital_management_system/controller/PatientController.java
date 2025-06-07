package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.PatientDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.PatientMapping;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import com.capgemini.hospital_management_system.repository.PatientRepository;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final AppointmentRepository appointmentRepository;
    private final PhysicianRepository physicianRepository;
    private final PatientRepository patientRepository;
    private final PatientMapping patientMapping;

    // GET /api/patient/ - Get list of all patients
    @GetMapping
    public ResponseEntity<Response<List<PatientDTO>>> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            throw new EntityNotFoundException("No patients found");
        }
        List<PatientDTO> patientDTOs = new ArrayList<>();
        for (Patient patient : patients) {
            patientDTOs.add(patientMapping.toDTO(patient));
        }
        Response<List<PatientDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "Patients retrieved successfully",
                patientDTOs,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
