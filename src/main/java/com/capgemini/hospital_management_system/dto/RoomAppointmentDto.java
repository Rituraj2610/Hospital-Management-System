package com.capgemini.hospital_management_system.dto;

import lombok.Data;

@Data
public class RoomAppointmentDto {

    private Integer roomNumber;
    private String roomType;
    private Boolean unavailable;
    // block and stays will be ignored in mapping
}
