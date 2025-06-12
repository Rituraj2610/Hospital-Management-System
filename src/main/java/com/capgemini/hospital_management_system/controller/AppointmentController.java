package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.AppointmentDatesDTO;
import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.PatientListMapper;
import com.capgemini.hospital_management_system.mapper.PatientMapper;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.projection.RoomAppointmentCount;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;

import com.capgemini.hospital_management_system.dto.*;
import com.capgemini.hospital_management_system.mapper.*;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Physician;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;
    private final PatientMapper patientMapper;
    private final PatientListMapper patientListMapper;
    private final AppointmentMapper appointmentMapper;
    private final PhysicianMapper physicianMapper;
    private final NurseMapper nurseMapper;


    @GetMapping
    public ResponseEntity<Response<PageResponse<AppointmentDTO>>> fetchAllAppointments(
            @PageableDefault(size = 20, sort = "appointmentId") Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentRepository.findAll(pageable);
        if (appointmentPage.isEmpty()) {
            throw new EntityNotFoundException("No Appointments found!");
        }

        List<AppointmentDTO> appointmentDTOList = appointmentPage.getContent().stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());

        PageResponse<AppointmentDTO> pageResponse = new PageResponse<>(
                appointmentDTOList,
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages(),
                appointmentPage.isFirst(),
                appointmentPage.isLast()
        );

        Response<PageResponse<AppointmentDTO>> response = Response.<PageResponse<AppointmentDTO>>builder()
                .status(200)
                .message("Found all appointments")
                .data(pageResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{startdate}")
    public ResponseEntity<Response<PageResponse<AppointmentDTO>>> fetchAppointmentsByStartBy(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startdate,
            @PageableDefault(size = 10, sort = "appointmentId") Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentRepository.findByStart(startdate, pageable);
        if (appointmentPage.isEmpty()) {
            throw new EntityNotFoundException("No appointments found!");
        }

        List<AppointmentDTO> appointmentDTOList = appointmentPage.getContent().stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());

        PageResponse<AppointmentDTO> pageResponse = new PageResponse<>(
                appointmentDTOList,
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages(),
                appointmentPage.isFirst(),
                appointmentPage.isLast()
        );

        Response<PageResponse<AppointmentDTO>> response = Response.<PageResponse<AppointmentDTO>>builder()
                .status(200)
                .message("Found all appointments")
                .data(pageResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/{appointmentid}")
    public ResponseEntity<Response<PatientAppointmentDTO>> fetchPatientByAppointmentId(@PathVariable Integer appointmentid) {
        Optional<Patient> optionalPatient = appointmentRepository.findPatientByAppointmentId(appointmentid);
        if (optionalPatient.isEmpty()) {
            throw new EntityNotFoundException("Patient not found!");
        }
        Response<PatientAppointmentDTO> response = Response.<PatientAppointmentDTO>builder()
                .status(200)
                .message("Found the patient's appointment")
                .data(patientMapper.toDto(optionalPatient.get()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/physician/{appointmentid}")
    public ResponseEntity<Response<PhysicianAppointmentDTO>> findPhysicianByAppointmentId(@PathVariable Integer appointmentid) {
        Optional<Physician> optionalPhysician = appointmentRepository.findPhysicianByAppointmentId(appointmentid);
        if (optionalPhysician.isEmpty()) {
            throw new EntityNotFoundException("Physician not found!");
        }
        Response<PhysicianAppointmentDTO> response = Response.<PhysicianAppointmentDTO>builder()
                .status(200)
                .message("Found the physician's appointment")
                .data(physicianMapper.toDto(optionalPhysician.get()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/nurse/{appointmentid}")
    public ResponseEntity<Response<NurseAppointmentDTO>> fetchNurseByAppointmentId(@PathVariable Integer appointmentid) {
        Optional<Nurse> optionalNurse = appointmentRepository.fetchNurseByAppointmentId(appointmentid);
        if (optionalNurse.isEmpty()) {
            throw new EntityNotFoundException("Nurse not found!");
        }
        Response<NurseAppointmentDTO> response = Response.<NurseAppointmentDTO>builder()
                .status(200)
                .message("Found the nurse's appointment")
                .data(nurseMapper.toDto(optionalNurse.get()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/examinationroom/{appointmentid}")
    public ResponseEntity<Response<String>> fetchRoomByAppointmentId(@PathVariable Integer appointmentid) {
        String room = appointmentRepository.findExaminationRoomByAppointmentId(appointmentid);
        if (room.isEmpty()) {
            throw new EntityNotFoundException("Room not found");
        }
        Response<String> response = Response.<String>builder()
                .status(200)
                .message("Found the room")
                .data(room)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/physician/patient/{patientid}")
    public ResponseEntity<Response<PageResponse<PhysicianAppointmentDTO>>> fetchPhysicianByPatientId(
            @PathVariable Integer patientid,
            @PageableDefault(size = 10, sort = "appointmentId") Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentRepository.findByPatient_Ssn(patientid, pageable);
        if (appointmentPage.isEmpty()) {
            throw new EntityNotFoundException("No appointment found for the given patient");
        }

        List<PhysicianAppointmentDTO> appointmentDTOList = appointmentPage.getContent().stream()
                .filter(appointment -> appointment.getPhysician() != null)
                .map(appointment -> physicianMapper.toDto(appointment.getPhysician()))
                .collect(Collectors.toList());

        PageResponse<PhysicianAppointmentDTO> pageResponse = new PageResponse<>(
                appointmentDTOList,
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages(),
                appointmentPage.isFirst(),
                appointmentPage.isLast()
        );

        Response<PageResponse<PhysicianAppointmentDTO>> response = Response.<PageResponse<PhysicianAppointmentDTO>>builder()
                .status(200)
                .message("Found the physicians")
                .data(pageResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/physician/{patientid}/{date}")
    public ResponseEntity<Response<PhysicianAppointmentDTO>> fetchPhysicianByPatientIdByStartDate(
            @PathVariable Integer patientid,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findByStartAndPatient_Ssn(date, patientid);
        if (optionalAppointment.isEmpty()) {
            throw new EntityNotFoundException("No appointment found");
        }

        PhysicianAppointmentDTO physicianAppointmentDTO = physicianMapper.toDto(optionalAppointment.get().getPhysician());
        Response<PhysicianAppointmentDTO> response = Response.<PhysicianAppointmentDTO>builder()
                .status(200)
                .message("Found the physician")
                .data(physicianAppointmentDTO)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/nurse/patient/{patientid}")
    public ResponseEntity<Response<PageResponse<NurseAppointmentDTO>>> fetchNurseByPatientId(
            @PathVariable Integer patientid,
            @PageableDefault(size = 10, sort = "appointmentId") Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentRepository.findByPatient_Ssn(patientid, pageable);
        if (appointmentPage.isEmpty()) {
            throw new EntityNotFoundException("No appointments found");
        }

        List<NurseAppointmentDTO> nurseAppointmentDTOList = appointmentPage.getContent().stream()
                .filter(appointment -> appointment.getPrepNurse() != null)
                .map(appointment -> nurseMapper.toDto(appointment.getPrepNurse()))
                .collect(Collectors.toList());

        PageResponse<NurseAppointmentDTO> pageResponse = new PageResponse<>(
                nurseAppointmentDTOList,
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages(),
                appointmentPage.isFirst(),
                appointmentPage.isLast()
        );

        Response<PageResponse<NurseAppointmentDTO>> response = Response.<PageResponse<NurseAppointmentDTO>>builder()
                .status(200)
                .message("Found the nurses")
                .data(pageResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/nurse/{patientid}/{date}")
    public ResponseEntity<Response<NurseAppointmentDTO>> fetchNurseByPatientIdByStartDate(
            @PathVariable Integer patientid,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findByStartAndPatient_Ssn(date, patientid);
        if (optionalAppointment.isEmpty()) {
            throw new EntityNotFoundException("No appointment found");
        }

        NurseAppointmentDTO nurseAppointmentDTO = nurseMapper.toDto(optionalAppointment.get().getPrepNurse());
        Response<NurseAppointmentDTO> response = Response.<NurseAppointmentDTO>builder()
                .status(200)
                .message("Found the nurse")
                .data(nurseAppointmentDTO)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response<String>> addAppointment(@RequestBody AppointmentPostDTO appointmentPostDTO){
//        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentPostDTO.getAppointmentId());
//        if(optionalAppointment.isEmpty()){
//            throw new EntityAlreadyExist("Appointment with same id already exists!");
//        }
        Appointment appointment = appointmentMapper.toEntity(appointmentPostDTO);
        appointmentRepository.save(appointment);
        Response<String> response = Response.<String>builder()
                .status(201)
                .message("Record created successfully")
                .data("Appointment with id: " + appointmentPostDTO.getAppointmentId() + " created successfully!")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

  // AMAN
  @GetMapping("/date/{patientId}")
  public ResponseEntity<Response<PageResponse<AppointmentDatesDTO>>> getDatesByPatientId(
          @PathVariable("patientId") Integer patientId,
          @PageableDefault(size = 10, sort = "start") Pageable pageable) {
      Page<Appointment> appointmentPage = appointmentRepository.findByPatient_Ssn(patientId, pageable);
      if (appointmentPage.isEmpty()) {
          throw new EntityNotFoundException("No appointment found for patient id " + patientId);
      }

      List<AppointmentDatesDTO> dates = appointmentPage.getContent().stream()
              .map(appointment -> new AppointmentDatesDTO(appointment.getStart()))
              .collect(Collectors.toList());

      PageResponse<AppointmentDatesDTO> pageResponse = new PageResponse<>(
              dates,
              appointmentPage.getNumber(),
              appointmentPage.getSize(),
              appointmentPage.getTotalElements(),
              appointmentPage.getTotalPages(),
              appointmentPage.isFirst(),
              appointmentPage.isLast()
      );

      Response<PageResponse<AppointmentDatesDTO>> response = new Response<>(
              HttpStatus.OK.value(),
              "Dates received successfully",
              pageResponse,
              LocalDateTime.now()
      );

      return new ResponseEntity<>(response, HttpStatus.OK);
  }

    @GetMapping("/patient/physician/{physicianId}")
    public ResponseEntity<Response<PageResponse<PatientAppointmentDTO>>> getPatientsByPhysicianId(
            @PathVariable("physicianId") Integer physicianId,
            @PageableDefault(size = 10, sort = "appointmentId") Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentRepository.findByPhysician_employeeId(physicianId, pageable);
        if (appointmentPage.isEmpty()) {
            throw new EntityNotFoundException("No patient found with physician id " + physicianId);
        }

        PageResponse<PatientAppointmentDTO> pageResponse = new PageResponse<>(
                patientListMapper.appointmentToPatientList(appointmentPage.getContent()),
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages(),
                appointmentPage.isFirst(),
                appointmentPage.isLast()
        );

        Response<PageResponse<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "Patients received successfully",
                pageResponse,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/{physicianId}/{date}")
    public ResponseEntity<Response<PageResponse<PatientAppointmentDTO>>> getPatientsByPhysicianIdAndDate(
            @PathVariable("physicianId") Integer physicianId,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @PageableDefault(size = 10, sort = "appointmentId") Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentRepository.findByPhysician_employeeIdAndStart(physicianId, date, pageable);
        if (appointmentPage.isEmpty()) {
            throw new EntityNotFoundException("No patient found with physician id " + physicianId + " and appointment date " + date);
        }

        PageResponse<PatientAppointmentDTO> pageResponse = new PageResponse<>(
                patientListMapper.appointmentToPatientList(appointmentPage.getContent()),
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages(),
                appointmentPage.isFirst(),
                appointmentPage.isLast()
        );

        Response<PageResponse<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "Patients received successfully",
                pageResponse,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient")
    public ResponseEntity<Response<PatientAppointmentDTO>> getPatientByPhysicianIdAndPatientId(
            @RequestParam Integer physicianId,
            @RequestParam Integer patientId) {
        Optional<Patient> patient = appointmentRepository.findByPhysicianIdAndPatientId(physicianId, patientId);
        if (patient.isEmpty()) {
            throw new EntityNotFoundException("No appointment found for patient id " + patientId + " and physician id " + physicianId);
        }
        Response<PatientAppointmentDTO> response = new Response<>(
                HttpStatus.OK.value(),
                "Patient received successfully",
                patientMapper.toDto(patient.get()),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/by-nurse")
    public ResponseEntity<Response<PageResponse<PatientAppointmentDTO>>> getPatientsByNurseId(
            @RequestParam Integer nurseId,
            @PageableDefault(size = 10, sort = "appointmentId") Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentRepository.findByPrepNurse_employeeId(nurseId, pageable);
        if (appointmentPage.isEmpty()) {
            throw new EntityNotFoundException("No patient found with Nurse id " + nurseId);
        }
        PageResponse<PatientAppointmentDTO> pageResponse = new PageResponse<>(
                patientListMapper.appointmentToPatientList(appointmentPage.getContent()),
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages(),
                appointmentPage.isFirst(),
                appointmentPage.isLast()
        );
        Response<PageResponse<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "Patients received successfully",
                pageResponse,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/by-nurse-and-patient")
    public ResponseEntity<Response<PatientAppointmentDTO>> getPatientByNurseIdAndPatientId(
            @RequestParam Integer nurseId,
            @RequestParam Integer patientId) {
        Optional<Patient> patient = appointmentRepository.findByNurseIdAndPatientId(nurseId, patientId);
        if (patient.isEmpty()) {
            throw new EntityNotFoundException("No appointment found for patient id " + patientId + " and Nurse id " + nurseId);
        }
        Response<PatientAppointmentDTO> response = new Response<>(
                HttpStatus.OK.value(),
                "Patient received successfully",
                patientMapper.toDto(patient.get()),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/by-nurse-and-date")
    public ResponseEntity<Response<PageResponse<PatientAppointmentDTO>>> getPatientsByNurseIdAndDate(
            @RequestParam Integer nurseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @PageableDefault(size = 10, sort = "appointmentId") Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentRepository.findByPrepNurse_employeeIdAndStart(nurseId, date, pageable);
        if (appointmentPage.isEmpty()) {
            throw new EntityNotFoundException("No patient found with nurseId " + nurseId + " and appointment date " + date);
        }
        PageResponse<PatientAppointmentDTO> pageResponse = new PageResponse<>(
                patientListMapper.appointmentToPatientList(appointmentPage.getContent()),
                appointmentPage.getNumber(),
                appointmentPage.getSize(),
                appointmentPage.getTotalElements(),
                appointmentPage.getTotalPages(),
                appointmentPage.isFirst(),
                appointmentPage.isLast()
        );
        Response<PageResponse<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "Patients received successfully",
                pageResponse,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/room/by-patient-and-date")
    public ResponseEntity<Response<String>> getRoomByPatientIdAndDate(
            @RequestParam Integer patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        Optional<String> room = appointmentRepository.findByPatientIdAndStartDate(patientId, date);
        if (room.isEmpty()) {
            throw new EntityNotFoundException("No room found with patientId " + patientId + " and appointment date " + date);
        }
        Response<String> response = new Response<>(
                HttpStatus.OK.value(),
                "Room received successfully",
                room.get(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/rooms/by-physician-and-date")
    public ResponseEntity<Response<PageResponse<String>>> geRoomByPhysicianIdAndDate(
            @RequestParam Integer physicianId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @PageableDefault(size = 10, sort = "appointmentId") Pageable pageable) {
        Page<String> roomPage = appointmentRepository.findByPhysicianIdAndStartDate(physicianId, date, pageable);
        if (roomPage.isEmpty()) {
            throw new EntityNotFoundException("No room found with physicianId " + physicianId + " and appointment date " + date);
        }
        PageResponse<String> pageResponse = new PageResponse<>(
                roomPage.getContent(),
                roomPage.getNumber(),
                roomPage.getSize(),
                roomPage.getTotalElements(),
                roomPage.getTotalPages(),
                roomPage.isFirst(),
                roomPage.isLast()
        );
        Response<PageResponse<String>> response = new Response<>(
                HttpStatus.OK.value(),
                "Rooms received successfully",
                pageResponse,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/rooms/by-nurse-and-date")
    public ResponseEntity<Response<PageResponse<String>>> getRoomByNurseIdAndDate(
            @RequestParam Integer nurseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @PageableDefault(size = 10, sort = "appointmentId") Pageable pageable) {
        Page<String> roomPage = appointmentRepository.findByNurseIdAndStartDate(nurseId, date, pageable);
        if (roomPage.isEmpty()) {
            throw new EntityNotFoundException("No room found with nurseId " + nurseId + " and appointment date " + date);
        }
        PageResponse<String> pageResponse = new PageResponse<>(
                roomPage.getContent(),
                roomPage.getNumber(),
                roomPage.getSize(),
                roomPage.getTotalElements(),
                roomPage.getTotalPages(),
                roomPage.isFirst(),
                roomPage.isLast()
        );
        Response<PageResponse<String>> response = new Response<>(
                HttpStatus.OK.value(),
                "Rooms received successfully",
                pageResponse,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/room/{appointmentId}")
    public ResponseEntity<Response<String>> updateRoomByAppointmentId(@PathVariable Integer appointmentId, @RequestBody String examinationRoom){
      Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
              () -> new EntityNotFoundException("No appointment found with appointmentId " + appointmentId)
      );
      appointment.setExaminationRoom(examinationRoom);
      appointmentRepository.save(appointment);
      Response<String> response = new Response<>(
              HttpStatus.OK.value(),
              "room updated successfully",
              examinationRoom,
              LocalDateTime.now()
      );
      return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/grouped")
    public ResponseEntity<List<Map<String, Object>>> getGroupedData() {
        List<Object[]> results = appointmentRepository.countAppointmentsPerPhysician();
        System.out.println("Endpoint HIT!");

        List<Map<String, Object>> response = results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("physicianName", row[0]);
            map.put("appointmentCount", row[1]);
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/roomcount")
    public List<RoomAppointmentCount> printRoomCounts() {
        List<RoomAppointmentCount> results = appointmentRepository.findAppointmentCountByRoom();

        return results;
    }
}
