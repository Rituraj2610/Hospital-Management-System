package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.AppointmentDatesDTO;
import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {
  private final AppointmentRepository appointmentRepository;


































































































































  @GetMapping("/date/{patientId}")
  public ResponseEntity<Response<List<AppointmentDatesDTO>>> getDatesByPatientId(@PathVariable("patientId") Integer patientId) {
       List<Appointment> appointments = appointmentRepository.findByPatient_ssn(patientId);
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
        List<PatientAppointmentDTO> patients = appointments.stream()
                .map(appointment -> new PatientAppointmentDTO(appointment.getPatient().getName(), appointment.getPatient().getAddress(),
                        appointment.getPatient().getPhone(), appointment.getPatient().getInsuranceId()))
                .toList();
        Response<List<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "patient recived successfully",
                patients,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/{physicianId}/{date}")
    public ResponseEntity<Response<List<PatientAppointmentDTO>>> getPatientsByPhysicianIdandDate
            (@PathVariable("physicianId") Integer physicianId, @PathVariable("date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date) {
        List<Appointment> appointments = appointmentRepository.findByPhysician_employeeIdAndStart(physicianId, date);
        List<PatientAppointmentDTO> patients = appointments.stream()
                .map(appointment -> new PatientAppointmentDTO(appointment.getPatient().getName(), appointment.getPatient().getAddress(),
                        appointment.getPatient().getPhone(), appointment.getPatient().getInsuranceId()))
                .toList();
        Response<List<PatientAppointmentDTO>> response = new Response<>(
                HttpStatus.OK.value(),
                "patient recived successfully",
                patients,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }







































































}
