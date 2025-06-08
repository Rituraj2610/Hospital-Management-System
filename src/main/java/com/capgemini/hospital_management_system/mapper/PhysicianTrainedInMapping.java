package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.PhysicianTrainedInDTO;
import com.capgemini.hospital_management_system.model.Physician;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhysicianTrainedInMapping {
    PhysicianTrainedInDTO toDTO(Physician physician);
    Physician toEntity(PhysicianTrainedInDTO physicianDTO);
    List<PhysicianTrainedInDTO> toDTOList(List<Physician> physicians);
}

