package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhysicianDto {

    private Integer employeeId;
    private String name;
    private String position;
    private Integer ssn;

    // No Set<Department>, Set<Patient>, etc. — clean DTO!
}
