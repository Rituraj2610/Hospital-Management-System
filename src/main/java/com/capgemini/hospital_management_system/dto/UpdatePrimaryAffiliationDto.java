package com.capgemini.hospital_management_system.dto;


import lombok.Data;

@Data
public class UpdatePrimaryAffiliationDto {

    private Integer departmentId;
    private Boolean primaryAffiliation;


}

