package com.capgemini.hospital_management_system.dto;


import lombok.Data;

@Data
public class CreateDepartmentDto {

    private Integer deptId;
    private String name;
    private Integer physicianId;

}
