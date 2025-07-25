package com.capgemini.hospital_management_system.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Nurse")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Nurse {
    @Id
    @Column(name = "EmployeeID")
    private Integer employeeId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Position", nullable = false)
    private String position;

    @Column(name = "Registered", nullable = false)
    private Boolean registered;

    @Column(name = "SSN", nullable = false)
    private Integer ssn;

    @OneToMany(mappedBy = "prepNurse", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Appointment> appointments = new     HashSet<>();

    @OneToMany(mappedBy = "nurse", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<OnCall> onCalls = new HashSet<>();

    @OneToMany(mappedBy = "assistingNurse", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Undergoes> undergoes = new HashSet<>();

}