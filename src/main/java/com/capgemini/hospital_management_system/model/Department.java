package com.capgemini.hospital_management_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Department")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @Column(name = "DepartmentID")
    private Integer departmentId;

    @Column(name = "Name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "Head", nullable = false)
    private Physician head;

    @ManyToMany(mappedBy = "affiliatedDepartments")
    private Set<Physician> physicians = new HashSet<>();

//    CONSTRAINT pk_Department PRIMARY KEY(DepartmentID),
//    CONSTRAINT fk_Department_Physician_EmployeeID FOREIGN KEY(Head) REFERENCES Physician(EmployeeID)

}
