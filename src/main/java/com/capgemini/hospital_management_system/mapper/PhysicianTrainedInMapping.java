package com.capgemini.hospital_management_system.mapper;


import com.capgemini.hospital_management_system.dto.PhysicianTrainedInDTO;
import com.capgemini.hospital_management_system.model.Physician;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhysicianTrainedInMapping {
    PhysicianTrainedInDTO toDTO(Physician physician);
}
