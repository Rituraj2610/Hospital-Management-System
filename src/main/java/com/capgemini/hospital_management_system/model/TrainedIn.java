package com.capgemini.hospital_management_system.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Trained_In")
@IdClass(TrainedInId.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainedIn {
    @Id
    @ManyToOne
    @JoinColumn(name = "Physician")
    private Physician physician;

    @Id
    @ManyToOne
    @JoinColumn(name = "Treatment")
    private Procedure treatment;

    @Column(name = "CertificationDate", nullable = false)
    private LocalDateTime certificationDate;

    @Column(name = "CertificationExpires", nullable = false)
    private LocalDateTime certificationExpires;

}
