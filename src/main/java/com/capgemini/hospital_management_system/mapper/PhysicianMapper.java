package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.PhysicianAppointmentDTO;
import com.capgemini.hospital_management_system.model.Physician;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhysicianMapper {

    @Mapping(target = "departments", ignore = true)
//    @Mapping(target = "affiliatedDepartments", ignore = true)
//    @Mapping(target = "trainedProcedures", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "undergoes", ignore = true)
    @Mapping(target = "patients", ignore = true)
    Physician toEntity(PhysicianAppointmentDTO physicianAppointmentDTO);

    PhysicianAppointmentDTO toDto(Physician physician);
}
