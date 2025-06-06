package com.capgemini.hospital_management_system.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Undergoes")
@IdClass(UndergoesId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Undergoes {
    @Id
    @ManyToOne
    @JoinColumn(name = "Patient")
    private Patient patient;

    @Id
    @ManyToOne
    @JoinColumn(name = "Procedures")
    private Procedure procedure;

    @Id
    @ManyToOne
    @JoinColumn(name = "Stay")
    private Stay stay;

    @Id
    @Column(name = "DateUndergoes")
    private LocalDateTime dateUndergoes;

    @ManyToOne
    @JoinColumn(name = "Physician", nullable = false)
    private Physician physician;

    @ManyToOne
    @JoinColumn(name = "AssistingNurse")
    private Nurse assistingNurse;

}