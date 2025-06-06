package com.capgemini.hospital_management_system.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Appointment")
public class Appointment {
    @Id
    @Column(name = "AppointmentID")
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "Patient", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "PrepNurse")
    private Nurse prepNurse;

    @ManyToOne
    @JoinColumn(name = "Physician", nullable = false)
    private Physician physician;

    @Column(name = "Starto", nullable = false)
    private LocalDateTime start;

    @Column(name = "Endo", nullable = false)
    private LocalDateTime end;

    @Column(name = "ExaminationRoom", nullable = false)
    private String examinationRoom;

    @OneToMany(mappedBy = "appointment")
    private Set<Prescribes> prescriptions = new HashSet<>();

}
