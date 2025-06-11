package com.capgemini.hospital_management_system.dto;


import lombok.Data;

@Data
public class AffiliatedWithDto {

    private Integer physicianId;
    private Integer departmentId;
    private Boolean primaryAffiliation;

}
