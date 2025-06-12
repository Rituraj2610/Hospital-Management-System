package com.capgemini.hospital_management_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "Head", nullable = false)
    @JsonBackReference
    private Physician head;

    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AffiliatedWith> affiliations = new HashSet<>();

    public void addPhysicianAffiliation(Physician physician, Boolean isPrimary) {
        AffiliatedWith affiliation = new AffiliatedWith();
        affiliation.setPhysician(physician);
        affiliation.setDepartment(this);
        affiliation.setPrimaryAffiliation(isPrimary);

        this.affiliations.add(affiliation);
        physician.getAffiliations().add(affiliation);
    }

    @Override
    public String toString() {
        return "Department{departmentId=" + departmentId + ", name=" + name + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department department = (Department) o;
        return Objects.equals(departmentId, department.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId);
    }
//    CONSTRAINT pk_Department PRIMARY KEY(DepartmentID),
//    CONSTRAINT fk_Department_Physician_EmployeeID FOREIGN KEY(Head) REFERENCES Physician(EmployeeID)

}
