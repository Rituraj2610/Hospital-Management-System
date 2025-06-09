package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.ProcedureDto;
import com.capgemini.hospital_management_system.model.Procedure;

public class ProcedureMapper {

    // Convert Procedure entity to ProcedureDto
    public static ProcedureDto toDto(Procedure procedure) {
        if (procedure == null) {
            return null;
        }

        ProcedureDto dto = new ProcedureDto();
        dto.setCode(procedure.getCode());
        dto.setName(procedure.getName());
        dto.setCost(procedure.getCost());

        return dto;
    }

    // Convert ProcedureDto to Procedure entity
    public static Procedure toEntity(ProcedureDto procedureDto) {
        if (procedureDto == null) {
            return null;
        }

        Procedure procedure = new Procedure();
        procedure.setCode(procedureDto.getCode());
        procedure.setName(procedureDto.getName());
        procedure.setCost(procedureDto.getCost());

        return procedure;
    }
}
