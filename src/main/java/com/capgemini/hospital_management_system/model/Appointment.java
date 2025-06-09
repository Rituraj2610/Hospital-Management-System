package com.capgemini.hospital_management_system.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Appointment")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Appointment {
    @Id
    @Column(name = "AppointmentID")
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "Patient", nullable = false)
    @JsonBackReference
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "PrepNurse")
    @JsonBackReference
    private Nurse prepNurse;

    @ManyToOne
    @JoinColumn(name = "Physician", nullable = false)
    @JsonBackReference
    private Physician physician;

    @Column(name = "Starto", nullable = false)
    private LocalDateTime start;

    @Column(name = "Endo", nullable = false)
    private LocalDateTime end;

    @Column(name = "ExaminationRoom", nullable = false)
    private String examinationRoom;

    @OneToMany(mappedBy = "appointment")
    @JsonManagedReference
    private Set<Prescribes> prescriptions = new HashSet<>();

}
