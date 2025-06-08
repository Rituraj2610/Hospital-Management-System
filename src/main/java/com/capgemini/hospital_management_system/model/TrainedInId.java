package com.capgemini.hospital_management_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainedInId implements Serializable {
    private Long physician; // Matches Physician.employeeId
    private Long treatment; // Matches Procedure.code
}
