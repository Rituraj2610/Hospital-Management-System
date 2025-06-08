package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainedInDTO {
    private Integer physicianId;
    private Integer treatmentId;
    private LocalDate certificationDate;
    private LocalDate certificationExpires;
}
