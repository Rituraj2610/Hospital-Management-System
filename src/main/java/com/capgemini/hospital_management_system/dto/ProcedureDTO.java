package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcedureDTO {
    private Integer code;
    private String name;
    private Double cost;
}
