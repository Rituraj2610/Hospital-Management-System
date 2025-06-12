package com.capgemini.hospital_management_system.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//@IdClass(TrainedInId.class)
@Entity
@Table(name = "Trained_In")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainedIn {
    @EmbeddedId
    private TrainedInId id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("physician")
    @JoinColumn(name = "Physician")
    @JsonBackReference
    private Physician physician;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("treatment")
    @JoinColumn(name = "Treatment")
    @JsonBackReference
    private Procedure treatment;

    @Column(name = "CertificationDate", nullable = false)
    private LocalDateTime certificationDate;

    @Column(name = "CertificationExpires", nullable = false)
    private LocalDateTime certificationExpires;
}
