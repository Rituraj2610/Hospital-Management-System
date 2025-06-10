package com.capgemini.hospital_management_system.dto;

import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.model.Prescribes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentPostDTO {
    @NotNull
    private Integer appointmentId;

    @NotNull(message = "Patient cant be null")
    private Integer ssn;


    private Integer employeeIdNurse;

    @NotNull(message = "Physician cant be null")
    private Integer employeeIdPhysician;

    @NotNull(message = "Start date cant be null")
    private LocalDateTime start;

    @NotNull(message = "End date cant be null")
    private LocalDateTime end;

    @NotNull(message = "Examination Room date cant be null")
    private String examinationRoom;

}
