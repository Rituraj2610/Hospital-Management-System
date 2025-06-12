package com.capgemini.hospital_management_system.dto;

import com.capgemini.hospital_management_system.model.OnCall;
import com.capgemini.hospital_management_system.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class testDto {
    private Integer blockFloor;
    private Integer blockCode;
}
