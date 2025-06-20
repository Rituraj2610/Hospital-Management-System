package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NurseDto {
	 private Integer employeeId;
	 private String name;
	 private String position;
	 private Boolean registered;
	 private Integer ssn;
}
