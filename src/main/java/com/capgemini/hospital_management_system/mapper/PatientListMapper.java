package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.model.Appointment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PatientListMapper {
    public List<PatientAppointmentDTO> appointmentToPatientList(List<Appointment> appointments){
        List<PatientAppointmentDTO> patients = appointments.stream()
                .map(appointment -> new PatientAppointmentDTO(appointment.getPatient().getName(), appointment.getPatient().getAddress(),
                        appointment.getPatient().getPhone(), appointment.getPatient().getInsuranceId(), appointment.getPatient().getSsn()))
                .toList();

        return patients;
    }

}
