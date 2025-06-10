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
@Table(name = "Procedures")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Procedure {
    @Id
    @Column(name = "Code")
    private Integer code;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Cost", nullable = false)
    private Double cost;

    @ManyToMany(mappedBy = "trainedProcedures")
    @JsonBackReference
    private Set<Physician> trainedPhysicians = new HashSet<>();

    @OneToMany(mappedBy = "procedure")
    @JsonManagedReference
    private Set<Undergoes> undergoes = new HashSet<>();

    public Procedure(int i, String s, double v) {
    }
}