package com.capgemini.hospital_management_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
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

    @OneToMany(mappedBy = "head", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy = "physician", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AffiliatedWith> affiliations = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Trained_In",
            joinColumns = @JoinColumn(name = "Physician"),
            inverseJoinColumns = @JoinColumn(name = "Treatment")
    )
    @JsonManagedReference
    private Set<Procedure> trainedProcedures = new HashSet<>();

    @OneToMany(mappedBy = "physician", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "physician", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Prescribes> prescriptions = new HashSet<>();

    @OneToMany(mappedBy = "physician", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Undergoes> undergoes = new HashSet<>();

    @OneToMany(mappedBy = "PCP", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Patient> patients = new HashSet<>();


    @Override
    public String toString() {
        return "Physician{employeeId=" + employeeId + ", name=" + name + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Physician)) return false;
        Physician physician = (Physician) o;
        return Objects.equals(employeeId, physician.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
//    CONSTRAINT pk_physician PRIMARY KEY(EmployeeID)
}
