package com.capgemini.hospital_management_system.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Stay")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stay {
    @Id
    @Column(name = "StayID")
    private Integer stayId;

    @ManyToOne
    @JoinColumn(name = "Patient", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "Room", nullable = false)
    private Room room;

    @Column(name = "StayStart", nullable = false)
    private LocalDateTime stayStart;

    @Column(name = "StayEnd", nullable = false)
    private LocalDateTime stayEnd;

    @OneToMany(mappedBy = "stay")
    private Set<Undergoes> undergoes = new HashSet<>();


}