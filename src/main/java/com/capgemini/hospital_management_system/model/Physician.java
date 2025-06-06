package com.capgemini.hospital_management_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Physician")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Physician {
    @Id
    @Column(name = "EmployeeID")
    private Integer employeeId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Position", nullable = false)
    private String position;

    @Column(name = "SSN", nullable = false)
    private Integer ssn;

    @OneToMany(mappedBy = "head")
    private Set<Department> departments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "Affiliated_With",
            joinColumns = @JoinColumn(name = "Physician"),
            inverseJoinColumns = @JoinColumn(name = "Department")
    )
    private Set<Department> affiliatedDepartments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "Trained_In",
            joinColumns = @JoinColumn(name = "Physician"),
            inverseJoinColumns = @JoinColumn(name = "Treatment")
    )
    private Set<Procedure> trainedProcedures = new HashSet<>();

    @OneToMany(mappedBy = "physician")
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "physician")
    private Set<Prescribes> prescriptions = new HashSet<>();

    @OneToMany(mappedBy = "physician")
    private Set<Undergoes> undergoes = new HashSet<>();

//    CONSTRAINT pk_physician PRIMARY KEY(EmployeeID)
}
