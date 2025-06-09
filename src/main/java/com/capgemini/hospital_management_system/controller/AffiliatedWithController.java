package com.capgemini.hospital_management_system.controller;


import com.capgemini.hospital_management_system.dto.PhysicianDepartmentDto;
import com.capgemini.hospital_management_system.model.AffiliatedWith;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.AffiliatedWithRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/affiliated_with")
public class AffiliatedWithController {

    @Autowired
    private AffiliatedWithRepository affiliatedWithRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/physicians/{deptid}")
        public ResponseEntity<List<PhysicianDepartmentDto>> getPhysiciansByDepartment(@PathVariable Integer deptid) {
            List<AffiliatedWith> affiliations = affiliatedWithRepository.findByDepartment_DepartmentId(deptid);

            List<PhysicianDepartmentDto> physicianDtos = affiliations.stream()
                    .map(aff -> modelMapper.map(aff.getPhysician(), PhysicianDepartmentDto.class))
                    .collect(Collectors.toList());


            return ResponseEntity.ok(physicianDtos);
        }



}
