package com.capgemini.hospital_management_system.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UndergoesId implements Serializable {
    private Integer patient; // Matches Patient.ssn
    private Integer procedure; // Matches Procedure.code
    private Integer stay; // Matches Stay.stayId
    private LocalDateTime dateUndergoes;

}
