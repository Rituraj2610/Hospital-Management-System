package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientAppointmentDTO {

    private String name;

    private String address;

    private String phone;

    private Integer insuranceId;
}
