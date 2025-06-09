package com.capgemini.hospital_management_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AffiliatedWithId implements Serializable {
    private Integer physician; // Matches Physician.employeeId
    private Integer department; // Matches Department.departmentId

}
