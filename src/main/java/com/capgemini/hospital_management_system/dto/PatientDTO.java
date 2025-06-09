package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private Integer ssn;
    private String name;
    private String address;
    private String phone;
    private Integer insuranceId;
    private Integer pcpId;

}