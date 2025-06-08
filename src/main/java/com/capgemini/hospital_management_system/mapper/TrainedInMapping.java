package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.TrainedInDTO;
import com.capgemini.hospital_management_system.model.TrainedIn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainedInMapping {
    @Mapping(source = "physician.employeeId", target = "physicianId")
    TrainedInDTO toDTO(TrainedIn trainedIn);

    @Mapping(source = "physicianId", target = "physician.employeeId")
    @Mapping(source = "treatmentId", target = "treatment.code")
    TrainedIn toEntity(TrainedInDTO trainedInDTO);

    List<TrainedInDTO> toDTOList(List<TrainedIn> trainedIns);
}
