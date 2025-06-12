package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTestDto {
    private Integer roomNumber;
    private String roomType;
    private testDto test;
    private Boolean unavailable;
}
