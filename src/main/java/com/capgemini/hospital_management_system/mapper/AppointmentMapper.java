package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.AppointmentDTO;
import com.capgemini.hospital_management_system.dto.AppointmentPostDTO;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.repository.NurseRepository;
import com.capgemini.hospital_management_system.repository.PatientRepository;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
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

    @Autowired
    protected PatientRepository patientRepository;

    @Autowired
    protected NurseRepository nurseRepository;

    @Autowired
    protected PhysicianRepository physicianRepository;

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

    public Appointment toEntity(AppointmentPostDTO dto) {
        if (dto == null) return null;

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(dto.getAppointmentId());
        appointment.setStart(dto.getStart());
        appointment.setEnd(dto.getEnd());
        appointment.setExaminationRoom(dto.getExaminationRoom());

        if (dto.getEmployeeIdPhysician() != null) {
            appointment.setPhysician(physicianRepository.findById(dto.getEmployeeIdPhysician()).get());
        }

        if (dto.getEmployeeIdNurse() != null) {
            appointment.setPrepNurse(nurseRepository.findById(dto.getEmployeeIdNurse()).get());
        }

        if (dto.getSsn() != null) {
            appointment.setPatient(patientRepository.findById(dto.getSsn()).get());
        }

        return appointment;
    }

}
