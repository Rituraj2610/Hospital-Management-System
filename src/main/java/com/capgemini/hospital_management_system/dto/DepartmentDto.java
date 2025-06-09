package com.capgemini.hospital_management_system.dto;


import lombok.Data;

@Data
public class DepartmentDto {

    private Integer departmentId;
    private String name;
    private PhysicianDepartmentDto physicianDetail;

}
