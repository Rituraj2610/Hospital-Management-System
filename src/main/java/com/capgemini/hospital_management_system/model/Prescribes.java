package com.capgemini.hospital_management_system.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Prescribes")
@IdClass(PrescribesId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prescribes {
    @Id
    @ManyToOne
    @JoinColumn(name = "Physician")
    private Physician physician;

    @Id
    @ManyToOne
    @JoinColumn(name = "Patient")
    private Patient patient;

    @Id
    @ManyToOne
    @JoinColumn(name = "Medication")
    private Medication medication;

    @Id
    @Column(name = "Date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "Appointment")
    private Appointment appointment;

    @Column(name = "Dose", nullable = false)
    private String dose;


}
