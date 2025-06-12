package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.TrainedInDTO;
import com.capgemini.hospital_management_system.dto.TrainedInPostDTO;
import com.capgemini.hospital_management_system.model.TrainedIn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {PhysicianTrainedInMapping.class, ProcedureTrainedInMapping.class}
)
public interface TrainedInMapping {

    @Mapping(target = "physicianId", source = "physician.employeeId")
    @Mapping(target = "procedureId", source = "treatment.code")
    TrainedInDTO toDTO(TrainedIn trainedIn);

    @Mapping(target = "physician.employeeId", source = "physicianId")
    @Mapping(target = "treatment.code", source = "procedureId")
    TrainedIn toEntity(TrainedInDTO trainedInDTO);

    List<TrainedInDTO> toDTOList(List<TrainedIn> trainedIns);

    @Mapping(target = "physician", source = "physician")
    @Mapping(target = "procedure", source = "treatment")
    TrainedInPostDTO toPostDTO(TrainedIn trainedIn);
}