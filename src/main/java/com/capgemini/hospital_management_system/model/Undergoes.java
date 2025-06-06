package com.capgemini.hospital_management_system.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Patient patient;

    @Id
    @ManyToOne
    @JoinColumn(name = "Procedures")
    @JsonBackReference
    private Procedure procedure;

    @Id
    @ManyToOne
    @JoinColumn(name = "Stay")
    @JsonBackReference
    private Stay stay;

    @Id
    @Column(name = "DateUndergoes")
    private LocalDateTime dateUndergoes;

    @ManyToOne
    @JoinColumn(name = "Physician", nullable = false)
    @JsonBackReference
    private Physician physician;

    @ManyToOne
    @JoinColumn(name = "AssistingNurse")
    @JsonBackReference
    private Nurse assistingNurse;

}