package com.capgemini.hospital_management_system.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OnCallId implements Serializable {
    private Integer nurse;
    private Integer blockFloor;
    private Integer blockCode;
    private LocalDateTime onCallStart;
    private LocalDateTime onCallEnd;


}
