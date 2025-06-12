package com.capgemini.hospital_management_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Patient")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Patient {
    @Id
    @Column(name = "SSN")
    private Integer ssn;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "Phone", nullable = false)
    private String phone;

    @Column(name = "InsuranceID", nullable = false)
    private Integer insuranceId;

    @ManyToOne
    @JoinColumn(name = "PCP", nullable = false)
    @JsonBackReference
    private Physician PCP;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Prescribes> prescriptions = new HashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Stay> stays = new HashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Undergoes> undergoes = new HashSet<>();

}
