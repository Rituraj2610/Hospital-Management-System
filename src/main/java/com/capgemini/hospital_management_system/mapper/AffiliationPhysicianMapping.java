package com.capgemini.hospital_management_system.mapper;

import com.capgemini.hospital_management_system.dto.AffiliatedPhysicianDto;
import com.capgemini.hospital_management_system.model.Physician;
import org.springframework.stereotype.Component;

@Component
public class AffiliationPhysicianMapping {
    public Physician mapToPhysician(AffiliatedPhysicianDto dto) {
        Physician physician = new Physician();
        physician.setEmployeeId(dto.getEmployeeId());
        physician.setName(dto.getName());
        physician.setPosition(dto.getPosition());
        physician.setSsn(dto.getSsn());
        return physician;
    }
}
