package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.PatientDTO;
import com.capgemini.hospital_management_system.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapping {
    PatientDTO toDTO(Patient patient);
    Patient toEntity(PatientDTO patientDTO);
}