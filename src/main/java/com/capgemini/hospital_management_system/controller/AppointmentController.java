package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.AppointmentDatesDTO;
import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.dto.RoomAppointmentDto;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.PatientListMapper;
import com.capgemini.hospital_management_system.mapper.PatientMapper;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Room;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import com.capgemini.hospital_management_system.dto.*;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.*;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;


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

    @GetMapping("/nurse/patient/{patientid}")
    public ResponseEntity<Response<List<NurseAppointmentDTO>>> fetchNurseByPatientId(@PathVariable Integer patientid){
        List<Appointment> appointmentList = appointmentRepository.findByPatient_Ssn(patientid);
        if(appointmentList.isEmpty()){
            throw  new EntityNotFoundException("No appoinments found");
        }
        List<NurseAppointmentDTO> nurseAppointmentDTOList = appointmentList.stream()
                .filter(appointment -> appointment.getPrepNurse() != null)
                .map(appointment -> nurseMapper.toDto(appointment.getPrepNurse()))
                .toList();

        Response<List<NurseAppointmentDTO>> response = Response.<List<NurseAppointmentDTO>>builder()
                .status(200)
                .message("Found the nurses")
                .data(nurseAppointmentDTOList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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

  // AMAN
  @GetMapping("/date/{patientId}")
  public ResponseEntity<Response<List<AppointmentDatesDTO>>> getDatesByPatientId(@PathVariable("patientId") Integer patientId) {
       List<Appointment> appointments = appointmentRepository.findByPatient_Ssn(patientId);
       if(appointments.isEmpty()) {
           throw new EntityNotFoundException("No appointment found for patient id " + patientId);
       }
       List<AppointmentDatesDTO> dates = appointments.stream()
               .map(appointment -> new AppointmentDatesDTO(appointment.getStart()))
               .toList();
      Response<List<AppointmentDatesDTO>> response = new Response<>(
              HttpStatus.OK.value(),
              "dates recived successfully",
              dates,
              LocalDateTime.now()
      );
      return new ResponseEntity<>(response, HttpStatus.OK);
   }

    @GetMapping("/patient/physician/{physicianId}")
    public ResponseEntity<Response<List<PatientAppointmentDTO>>> getPatientsByPhysicianId(@PathVariable("physicianId") Integer physicianId) {
        List<Appointment> appointments = appointmentRepository.findByPhysician_employeeId(physicianId);
        if(appointments.isEmpty()) {
            throw new EntityNotFoundException("No patient found with physician id " + physicianId);
        }

        Response<List<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "patient recived successfully",
                patientListMapper.appointmentToPatientList(appointments),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/{physicianId}/{date}")
    public ResponseEntity<Response<List<PatientAppointmentDTO>>> getPatientsByPhysicianIdAndDate
            (@PathVariable("physicianId") Integer physicianId, @PathVariable("date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date) {
        List<Appointment> appointments = appointmentRepository.findByPhysician_employeeIdAndStart(physicianId, date);
        if(appointments.isEmpty()) {
            throw new EntityNotFoundException("No patient found with physician id " + physicianId + " and appointment date " + date);
        }

        Response<List<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "patient recived successfully",
                patientListMapper.appointmentToPatientList(appointments),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient")
    public ResponseEntity<Response<PatientAppointmentDTO>> getPatientByPhysicianIdAndPatientId
            (@RequestParam Integer physicianId, @RequestParam Integer patientId) {
        Optional<Patient> patient = appointmentRepository.findByPhysicianIdAndPatientId(physicianId, patientId);
        if(patient.isEmpty()) {
            throw new EntityNotFoundException("No appointment found for patient id " + patientId + " and physician id " + physicianId);
        }
        Response<PatientAppointmentDTO> response = new Response<>(
                HttpStatus.OK.value(),
                "patient recived successfully",
                patientMapper.toDto(patient.get()),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/by-nurse")
    public ResponseEntity<Response<List<PatientAppointmentDTO>>> getPatientsByNurseId(@RequestParam Integer nurseId) {
        List<Appointment> appointments = appointmentRepository.findByPrepNurse_employeeId(nurseId);
        if(appointments.isEmpty()) {
            throw new EntityNotFoundException("No patient found with Nurse id " + nurseId);
        }
        Response<List<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "patient recived successfully",
                patientListMapper.appointmentToPatientList(appointments),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/by-nurse-and-patient")
    public ResponseEntity<Response<PatientAppointmentDTO>> getPatientByNurseIdAndPatientId
            (@RequestParam Integer NurseId, @RequestParam Integer patientId) {
        Optional<Patient> patient = appointmentRepository.findByNurseIdAndPatientId(NurseId, patientId);
        if(patient.isEmpty()) {
            throw new EntityNotFoundException("No appointment found for patient id " + patientId + " and Nurse id " + NurseId);
        }
        Response<PatientAppointmentDTO> response = new Response<>(
                HttpStatus.OK.value(),
                "patient recived successfully",
                patientMapper.toDto(patient.get()),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/by-nurse-and-date")
    public ResponseEntity<Response<List<PatientAppointmentDTO>>> getPatientsByNurseIdAndDate
            (@RequestParam Integer nurseId, @RequestParam@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date) {
        List<Appointment> appointments = appointmentRepository.findByPrepNurse_employeeIdAndStart(nurseId, date);
        if (appointments.isEmpty()) {
            throw new EntityNotFoundException("No patient found with nurseId" + nurseId + " and appointment date " + date);
        }
        Response<List<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "patient recived successfully",
                patientListMapper.appointmentToPatientList(appointments),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/room/by-patient-and-date")
    public ResponseEntity<Response<String>> getRoomByPatientIdAndDate
            (@RequestParam Integer patientId, @RequestParam@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date) {
        Optional<String> room = appointmentRepository.findByPatientIdAndStartDate(patientId, date);
        if (room.isEmpty()) {
            throw new EntityNotFoundException("No room found with patientId" + patientId + " and appointment date " + date);
        }
        Response<String> response = new Response<>(
                HttpStatus.OK.value(),
                "room recived successfully",
                 room.get(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/rooms/by-physician-and-date")
    public ResponseEntity<Response<List<String>>> geRoomByPhysicianIdAndDate
            (@RequestParam Integer physicianId, @RequestParam@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date) {
        List<String> room = appointmentRepository.findByPhysicianIdAndStartDate(physicianId, date);
        if (room.isEmpty()) {
            throw new EntityNotFoundException("No room found with physicianId" + physicianId + " and appointment date " + date);
        }
        Response<List<String>> response = new Response<>(
                HttpStatus.OK.value(),
                "room recived successfully",
                room,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/rooms/by-nurse-and-date")
    public ResponseEntity<Response<List<String>>> getRoomByNurseIdAndDate
            (@RequestParam Integer nurseId, @RequestParam@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date) {
        List<String> room = appointmentRepository.findByNurseIdAndStartDate(nurseId, date);
        if (room.isEmpty()) {
            throw new EntityNotFoundException("No room found with nurseId" + nurseId + " and appointment date " + date);
        }
        Response<List<String>> response = new Response<>(
                HttpStatus.OK.value(),
                "room recived successfully",
                room,
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
}
