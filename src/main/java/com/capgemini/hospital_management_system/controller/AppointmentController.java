package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.AppointmentDatesDTO;
import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.PatientListMapper;
import com.capgemini.hospital_management_system.mapper.PatientMapper;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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








































































































































































































  @GetMapping("/date/{patientId}")
  public ResponseEntity<Response<List<AppointmentDatesDTO>>> getDatesByPatientId(@PathVariable("patientId") Integer patientId) {
       List<Appointment> appointments = appointmentRepository.findByPatient_ssn(patientId);
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

    @GetMapping("/patient/{physicianId}")
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






































































}
