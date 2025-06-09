package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NurseAppointmentDTO {

    private String name;

    private String position;

    private Boolean registered;

    private Integer ssn;
}
