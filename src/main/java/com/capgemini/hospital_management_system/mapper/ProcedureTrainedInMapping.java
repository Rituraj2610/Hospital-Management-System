package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.ProcedureTrainedInDTO;
import com.capgemini.hospital_management_system.model.Procedure;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProcedureTrainedInMapping {
    ProcedureTrainedInDTO toDTO(Procedure procedure);
    Procedure toEntity(ProcedureTrainedInDTO procedureDTO);
    List<ProcedureTrainedInDTO> toDTOList(List<Procedure> procedures);
}
