package com.capgemini.hospital_management_system.dto;

import lombok.Data;

@Data
public class AffiliatedPhysicianDto {
    private Integer employeeId;
    private String name;
    private String position;
    private Integer ssn;
    private Boolean primaryAffiliation;
}