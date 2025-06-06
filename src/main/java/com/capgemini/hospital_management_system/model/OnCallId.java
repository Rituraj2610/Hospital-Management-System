package com.capgemini.hospital_management_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OnCallId implements Serializable {
    private Integer nurse;
    private Integer blockFloor;
    private Integer blockCode;
    private LocalDateTime onCallStart;
    private LocalDateTime onCallEnd;


}
