package com.capgemini.hospital_management_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Affiliated_With")
@IdClass(AffiliatedWithId.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AffiliatedWith {
    @Id
    @ManyToOne
    @JoinColumn(name = "Physician")
    private Physician physician;

    @Id
    @ManyToOne
    @JoinColumn(name = "Department")
    private Department department;

    @Column(name = "PrimaryAffiliation", nullable = false)
    private Boolean primaryAffiliation;


//    CONSTRAINT fk_Affiliated_With_Physician_EmployeeID FOREIGN KEY(Physician) REFERENCES Physician(EmployeeID),
//    CONSTRAINT fk_Affiliated_With_Department_DepartmentID FOREIGN KEY(Department) REFERENCES Department(DepartmentID),
//    PRIMARY KEY(Physician, Department)
}
