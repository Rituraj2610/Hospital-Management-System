package com.capgemini.hospital_management_system.dto;

import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentDTO {
    private LocalDateTime start;
    private LocalDateTime end;
    private String examinationRoom;
    private PhysicianAppointmentDTO physician;
    private NurseAppointmentDTO nurse;
    private PatientAppointmentDTO patient;

    // constructors, getters, setters
}