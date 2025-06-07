package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.AppointmentDTO;
import com.capgemini.hospital_management_system.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AppointmentMapper {

    @Autowired
    protected PhysicianMapper physicianMapper;

    @Autowired
    protected NurseMapper nurseMapper;

    @Autowired
    protected PatientMapper patientMapper;

    @Mapping(target = "physician", ignore = true)
    @Mapping(target = "nurse", ignore = true)
    @Mapping(target = "patient", ignore = true)
    public abstract AppointmentDTO toDtoBasic(Appointment appointment);

    public AppointmentDTO toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentDTO dto = toDtoBasic(appointment);

        dto.setPhysician(appointment.getPhysician() != null ?
                physicianMapper.toDto(appointment.getPhysician()) : null);

        dto.setNurse(appointment.getPrepNurse() != null ?
                nurseMapper.toDto(appointment.getPrepNurse()) : null);

        dto.setPatient(appointment.getPatient() != null ?
                patientMapper.toDto(appointment.getPatient()) : null);

        return dto;
    }
}
