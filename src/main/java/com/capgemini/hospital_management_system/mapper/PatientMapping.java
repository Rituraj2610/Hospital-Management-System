package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.PatientDTO;
import com.capgemini.hospital_management_system.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapping {

    public PatientDTO toDTO(Patient patient) {
        if (patient == null) {
            return null;
        }
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setSsn(patient.getSsn());
        patientDTO.setName(patient.getName());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setPhone(patient.getPhone());
        patientDTO.setInsuranceId(patient.getInsuranceId());
        patientDTO.setPcpId(patient.getPCP() != null ? patient.getPCP().getEmployeeId() : null);
        return patientDTO;
    }

    public Patient toEntity(PatientDTO patientDTO) {
        if (patientDTO == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setSsn(patientDTO.getSsn());
        patient.setName(patientDTO.getName());
        patient.setAddress(patientDTO.getAddress());
        patient.setPhone(patientDTO.getPhone());
        patient.setInsuranceId(patientDTO.getInsuranceId());
        // PCP is set in the controller
        return patient;
    }
}