package com.capgemini.hospital_management_system.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescribesId implements Serializable {
    private Integer physician;
    private Integer patient;
    private Integer medication;
    private LocalDateTime date;

}
