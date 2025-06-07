package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.AppointmentDTO;
import com.capgemini.hospital_management_system.dto.NurseAppointmentDTO;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Nurse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NurseMapper {

    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "onCalls", ignore = true)
    @Mapping(target = "undergoes", ignore = true)
    Nurse toEntity(NurseAppointmentDTO nurseAppointmentDTO);

    NurseAppointmentDTO toDto(Nurse nurse);
}
