package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.NurseDto;
import com.capgemini.hospital_management_system.model.Nurse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class NurseCustomMapper {

	//post mapper
	
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

    public List<NurseDto> toDtoList(List<Nurse> nurses) {
        return nurses.stream()
                     .map(this::toDto) 
                     .collect(Collectors.toList());
    }

	public NurseDto toDto(Optional<Nurse> nurse) {
		// TODO Auto-generated method stub
		return this.toDto(nurse);
	}

	//updating
	public void updateNurseFromDto(NurseDto dto, Nurse entity) {
	    if (dto == null || entity == null) return;

	    if (dto.getName() != null) {
	        entity.setName(dto.getName());
	    }
	    if (dto.getPosition() != null) {
	        entity.setPosition(dto.getPosition());
	    }
	    if (dto.getSsn() != null) {
	        entity.setSsn(dto.getSsn());
	    }
	    if (dto.getRegistered() != null) {
	        entity.setRegistered(dto.getRegistered());
	    }
	}


	

    
    
    
}
