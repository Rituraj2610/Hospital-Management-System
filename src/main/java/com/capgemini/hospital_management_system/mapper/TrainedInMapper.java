package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.TrainedInDto;
import com.capgemini.hospital_management_system.model.TrainedIn;
import org.springframework.stereotype.Component;

@Component
public class TrainedInMapper {

    public TrainedIn toEntity(TrainedInDto dto) {
        if (dto == null) {
            return null;
        }

        TrainedIn entity = new TrainedIn();
        entity.setCertificationDate(dto.getCertificationDate());
        entity.setCertificationExpires(dto.getCertificationExpires());
        return entity;
    }

    public TrainedInDto toDto(TrainedIn entity) {
        if (entity == null) {
            return null;
        }

        TrainedInDto dto = new TrainedInDto();
        dto.setPhysicianId(entity.getPhysician().getEmployeeId());
        dto.setTreatmentId(entity.getTreatment().getCode());
        dto.setCertificationDate(entity.getCertificationDate());
        dto.setCertificationExpires(entity.getCertificationExpires());
        return dto;
    }

}
