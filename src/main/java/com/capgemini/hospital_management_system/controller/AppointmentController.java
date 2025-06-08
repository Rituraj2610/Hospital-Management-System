package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.*;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.AppointmentMapper;
import com.capgemini.hospital_management_system.mapper.NurseMapper;
import com.capgemini.hospital_management_system.mapper.PatientMapper;
import com.capgemini.hospital_management_system.mapper.PhysicianMapper;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientMapper patientMapper;
    private final PhysicianMapper physicianMapper;
    private final NurseMapper nurseMapper;

    @GetMapping
    public ResponseEntity<Response<List<AppointmentDTO>>> fetchAllAppointments(){
        List<Appointment> appointmentList = appointmentRepository.findAll();
        if(appointmentList.isEmpty()){
            throw new EntityNotFoundException("No Appointments found!");
        }

        List<AppointmentDTO> appointmentDTOList = appointmentList
                .stream()
                .map(appointmentMapper::toDto)
                .toList();

        Response<List<AppointmentDTO>> response = Response.<List<AppointmentDTO>>builder()
                .status(200)
                .message("Found all appointments")
                .data(appointmentDTOList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{startdate}")
    public ResponseEntity<Response<List<AppointmentDTO>>> fetchAppointmentsByStartBy(@PathVariable LocalDateTime startdate){
        List<Appointment> appointmentList = appointmentRepository.findByStart(startdate);
        if(appointmentList.isEmpty()){
            throw new EntityNotFoundException("No appointments found!");
        }
        List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                .map(appointmentMapper::toDto)
                .toList();

        Response<List<AppointmentDTO>> response = Response.<List<AppointmentDTO>>builder()
                .status(200)
                .message("Found all appointments")
                .data(appointmentDTOList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/{appointmentid}")
    public ResponseEntity<Response<PatientAppointmentDTO>> fetchPatientByAppointmentId(@PathVariable Integer appointmentid){
        Optional<Patient> optionalPatient = appointmentRepository.findPatientByAppointmentId(appointmentid);
        if(optionalPatient.isEmpty()){
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
    public ResponseEntity<Response<PhysicianAppointmentDTO>> findPhysicianByAppointmentId(@PathVariable Integer appointmentid){
        Optional<Physician> optionalPhysician = appointmentRepository.findPhysicianByAppointmentId(appointmentid);
        if(optionalPhysician.isEmpty()){
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
    public ResponseEntity<Response<NurseAppointmentDTO>> fetchNurseByAppointmentId(@PathVariable Integer appointmentid){
        Optional<Nurse> optionalNurse = appointmentRepository.fetchNurseByAppointmentId(appointmentid);
        if(optionalNurse.isEmpty()){
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
    public ResponseEntity<Response<String>> fetchRoomByAppointmentId(@PathVariable Integer appointmentid){
        String room = appointmentRepository.findExaminationRoomByAppointmentId(appointmentid);
        if(room.isEmpty()){
            throw  new EntityNotFoundException("Room not found");
        }
        Response<String> response = Response.<String>builder()
                .status(200)
                .message("Found the room")
                .data(room)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/physician/patient/{patientid}")
    public ResponseEntity<Response<List<PhysicianAppointmentDTO>>> fetchPhysicianByPatientId(@PathVariable Integer patientid){
        List<Appointment> appointmentList = appointmentRepository.findByPatient_Ssn(patientid);
        if(appointmentList.isEmpty()){
            throw  new EntityNotFoundException("No appointment found for the given patient");
        }
        List<PhysicianAppointmentDTO> appointmentDTOList = appointmentList.stream()
                .filter(appointment -> appointment.getPhysician() != null)
                .map(appointment -> physicianMapper.toDto(appointment.getPhysician()))
                .toList();

        Response<List<PhysicianAppointmentDTO>> response = Response.<List<PhysicianAppointmentDTO>>builder()
                .status(200)
                .message("Found the physicians")
                .data(appointmentDTOList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/physician/{patientid}/{date}")
    public ResponseEntity<Response<PhysicianAppointmentDTO>> fetchPhysicianByPatientIdByStartDate(@PathVariable Integer patientid, @PathVariable LocalDateTime date){
        Optional<Appointment> optionalAppointment = appointmentRepository.findByStartAndPatient_Ssn(date, patientid);
        if(optionalAppointment.isEmpty()){
            throw  new EntityNotFoundException("No appointment found");
        }

        PhysicianAppointmentDTO physicianAppointmentDTO = physicianMapper.toDto(optionalAppointment.get().getPhysician());
        Response<PhysicianAppointmentDTO> response = Response.<PhysicianAppointmentDTO>builder()
                .status(200)
                .message("Found the physicians")
                .data(physicianAppointmentDTO)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @GetMapping("/nurse/patient/{patientid}")
//    public ResponseEntity<Response<List<NurseAppointmentDTO>>> fetchNurseByPatientId(@PathVariable Integer patientid){
//        List<Appointment> appointmentList = appointmentRepository.findByPatient_Ssn(patientid);
//        if(appointmentList.isEmpty()){
//            throw  new EntityNotFoundException("No appoinments found");
//        }
//        List<NurseAppointmentDTO> nurseAppointmentDTOList = appointmentList.stream()
//                .filter(appointment -> appointment.getPrepNurse() != null)
//                .map(appointment -> nurseMapper.toDto(appointment.getPrepNurse()))
//                .toList();
//
//        Response<List<NurseAppointmentDTO>> response = Response.<List<NurseAppointmentDTO>>builder()
//                .status(200)
//                .message("Found the nurses")
//                .data(nurseAppointmentDTOList)
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @GetMapping("/nurse/{patientid}/{date}")
    public ResponseEntity<Response<NurseAppointmentDTO>> fetchNurseByPatientIdByStartDate(@PathVariable Integer patientid, @PathVariable LocalDateTime date){
        Optional<Appointment> optionalAppointment = appointmentRepository.findByStartAndPatient_Ssn(date, patientid);
        if(optionalAppointment.isEmpty()){
            throw  new EntityNotFoundException("No appointment found");
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
































































































































































































}
