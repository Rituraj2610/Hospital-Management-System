package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.NurseDto;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.mapper.NurseMapper;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.repository.NurseRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class NurseController {

    private final NurseRepository nurseRepository;
    private final NurseMapper nurseMapper;

    @PostMapping("/nurse")
    public ResponseEntity<Response<NurseDto>> addNurseDetails(@RequestBody NurseDto req) {

        Nurse nurse = nurseMapper.toEntity(req);
        nurseRepository.save(nurse);
        NurseDto responseDto = nurseMapper.toDto(nurse);

        return ResponseEntity.ok(
            new Response<>(
                200,
                "Nurse saved successfully",
                responseDto,
                LocalDateTime.now()
            )
        );
    }
}
