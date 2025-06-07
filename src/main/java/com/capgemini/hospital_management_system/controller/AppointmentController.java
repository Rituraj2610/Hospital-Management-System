package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.*;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.AppointmentMapper;
import com.capgemini.hospital_management_system.mapper.NurseMapper;
import com.capgemini.hospital_management_system.mapper.PatientMapper;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

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










































































































































































































}
