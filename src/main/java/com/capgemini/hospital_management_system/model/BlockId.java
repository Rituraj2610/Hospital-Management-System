package com.capgemini.hospital_management_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockId implements Serializable {
    private Integer blockFloor;
    private Integer blockCode;
}
