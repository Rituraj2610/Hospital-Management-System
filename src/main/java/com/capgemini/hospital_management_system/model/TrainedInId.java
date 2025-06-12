package com.capgemini.hospital_management_system.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class TrainedInId implements Serializable {
    private Integer physician; // Matches Physician.employeeId
    private Integer treatment; // Matches Procedure.code
}
