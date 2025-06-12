package com.capgemini.hospital_management_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Entity
@Table(name = "Affiliated_With")
@IdClass(AffiliatedWithId.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AffiliatedWith {
    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "physician")
    private Physician physician;

    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "department")
    @JsonIgnore
    private Department department;

    @Column(name = "PrimaryAffiliation", nullable = false)
    private Boolean primaryAffiliation;

    @Override
    public String toString() {
        return "AffiliatedWith{physicianId=" + (physician != null ? physician.getEmployeeId() : null) +
                ", departmentId=" + (department != null ? department.getDepartmentId() : null) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AffiliatedWith)) return false;
        AffiliatedWith that = (AffiliatedWith) o;
        return Objects.equals(physician != null ? physician.getEmployeeId() : null, that.physician != null ? that.physician.getEmployeeId() : null) &&
                Objects.equals(department != null ? department.getDepartmentId() : null, that.department != null ? that.department.getDepartmentId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(physician != null ? physician.getEmployeeId() : null, department != null ? department.getDepartmentId() : null);
    }


//    CONSTRAINT fk_Affiliated_With_Physician_EmployeeID FOREIGN KEY(Physician) REFERENCES Physician(EmployeeID),
//    CONSTRAINT fk_Affiliated_With_Department_DepartmentID FOREIGN KEY(Department) REFERENCES Department(DepartmentID),
//    PRIMARY KEY(Physician, Department)
}
