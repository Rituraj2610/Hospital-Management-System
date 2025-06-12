package com.capgemini.hospital_management_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDepartmentDto {

    private Integer deptId;
    private String name;
    private PhysicianAppointmentDTO head;
    private List<AffiliatedPhysicianDto> affiliatedPhysicians;

}
