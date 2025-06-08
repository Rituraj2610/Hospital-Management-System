package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainedInDto {
    private Long physicianId;
    private Long treatmentId;
    private LocalDateTime certificationDate;
    private LocalDateTime certificationExpires;
}
