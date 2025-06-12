package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainedInUpdateDTO {
    private ProcedureDTO procedure;
    private LocalDateTime certificationDate;
    private LocalDateTime certificationExpires;
}
