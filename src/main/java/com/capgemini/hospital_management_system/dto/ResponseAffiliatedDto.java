package com.capgemini.hospital_management_system.dto;

import com.capgemini.hospital_management_system.model.Department;
import com.capgemini.hospital_management_system.model.Physician;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAffiliatedDto {
    private String physician;
    private String department;
    private Boolean primaryAffiliation;
}
