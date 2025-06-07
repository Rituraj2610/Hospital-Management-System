package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.NurseDto;
import com.capgemini.hospital_management_system.model.Nurse;
import org.springframework.stereotype.Component;

@Component
public class NurseMapper {

    public NurseDto toDto(Nurse nurse) {
        if (nurse == null) return null;

        NurseDto dto = new NurseDto();
        dto.setEmployeeId(nurse.getEmployeeId());
        dto.setName(nurse.getName());
        dto.setPosition(nurse.getPosition());
        dto.setRegistered(nurse.getRegistered());
        dto.setSsn(nurse.getSsn());

        return dto;
    }

    public Nurse toEntity(NurseDto dto) {
        if (dto == null) return null;

        Nurse nurse = new Nurse();
        nurse.setEmployeeId(dto.getEmployeeId());
        nurse.setName(dto.getName());
        nurse.setPosition(dto.getPosition());
        nurse.setRegistered(dto.getRegistered());
        nurse.setSsn(dto.getSsn());

        return nurse;
    }
}
