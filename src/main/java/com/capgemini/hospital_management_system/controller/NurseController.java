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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class NurseController {

    private final NurseRepository nurseRepository;
    private final NurseMapper nurseMapper;

    // Saving nurse details
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

    // Getting all nurse details
    @GetMapping("/nurse")
    public ResponseEntity<Response<List<NurseDto>>> getNurseDetails() {
        List<Nurse> nurses = nurseRepository.findAll();

        if (nurses.isEmpty()) {
            return ResponseEntity.status(404).body(
                new Response<>(
                    404,
                    "No nurses found",
                    null,
                    LocalDateTime.now()
                )
            );
        }

        List<NurseDto> responseDto = nurseMapper.toDtoList(nurses);
        return ResponseEntity.ok(new Response<>(
            200,
            "Nurses retrieved successfully",
            responseDto,
            LocalDateTime.now()
        ));
    }

    // Getting nurse by ID
    @GetMapping("/nurse/{empid}")
    public ResponseEntity<Response<NurseDto>> getNurseById(@PathVariable("empid") Integer id) {
        Optional<Nurse> nurse = nurseRepository.findById(id);

        if (nurse.isEmpty()) {
            return ResponseEntity.status(404).body(
                new Response<>(
                    404,
                    "Nurse with employee ID " + id + " not found",
                    null,
                    LocalDateTime.now()
                )
            );
        }

        NurseDto responseDto = nurseMapper.toDto(nurse.get());
        return ResponseEntity.ok(new Response<>(
            200,
            "Nurse with employee ID " + responseDto.getEmployeeId() + " retrieved successfully",
            responseDto,
            LocalDateTime.now()
        ));
    }
}
