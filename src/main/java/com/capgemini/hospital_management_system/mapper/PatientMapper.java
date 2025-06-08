package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.NurseAppointmentDTO;
import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "PCP", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "stays", ignore = true)
    @Mapping(target = "undergoes", ignore = true)
    Patient toEntity(PatientAppointmentDTO patientAppointmentDTO);

    PatientAppointmentDTO toDto(Patient patient);
}
