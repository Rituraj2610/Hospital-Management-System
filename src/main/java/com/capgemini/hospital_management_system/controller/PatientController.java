package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.PatientDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.exception.EntityAlreadyExist;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.PatientMapping;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
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
import java.util.stream.Collectors;


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

    // GET /api/patient/{physicianid} - Retrieves patients associated with a specific physician
    @GetMapping("/{physicianid}")
    public ResponseEntity<Response<List<PatientDTO>>> getPatientsByPhysician(@PathVariable Integer physicianid) {
        List<Patient> patient = patientRepository.findByPCP_employeeId(physicianid)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Physician with ID " + physicianid + " not found"));

        if (patient.isEmpty()) {
            throw new EntityNotFoundException("No patients found under ID " + physicianid);
        }

        List<PatientDTO> patients = patientRepository.findAll()
                .stream()
                .filter(p -> physicianid.equals(p.getPCP().getEmployeeId()))
                .map(patientMapping::toDTO)
                .collect(Collectors.toList());

        Response<List<PatientDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "Patients for physician " + physicianid + " retrieved successfully",
                patients,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
