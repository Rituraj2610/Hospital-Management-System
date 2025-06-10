package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainedInDTO {
    private Integer physicianId;
   // private Integer treatmentId;
    private Integer procedureId;
    private LocalDateTime certificationDate;
    private LocalDateTime certificationExpires;
}
