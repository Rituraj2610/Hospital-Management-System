package com.capgemini.hospital_management_system.appointment_tests;

import com.capgemini.hospital_management_system.controller.AppointmentController;
import com.capgemini.hospital_management_system.dto.AppointmentDTO;
import com.capgemini.hospital_management_system.dto.NurseAppointmentDTO;
import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.dto.PhysicianAppointmentDTO;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.AppointmentMapper;
import com.capgemini.hospital_management_system.model.*;
import com.capgemini.hospital_management_system.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentMapper appointmentMapper;

    @Test
    @DisplayName("Get all appointments")
    void fetchAllAppointments_ReturnsAppointmentsList() throws Exception {
        Appointment appointment1 = new Appointment(111, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00:00"), LocalDateTime.parse("2008-04-24T10:00:00"), "e1", new HashSet<>());
        Appointment appointment2 = new Appointment(222, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00:00"), LocalDateTime.parse("2008-04-24T10:00:00"), "e2", new HashSet<>());
        List<Appointment> appointments = List.of(appointment1, appointment2);

        AppointmentDTO appointmentDTO1 = new AppointmentDTO(LocalDateTime.parse("2008-04-24T10:00:00"), LocalDateTime.parse("2008-04-24T10:00:00"), "e1", new PhysicianAppointmentDTO(), new NurseAppointmentDTO(), new PatientAppointmentDTO());
        AppointmentDTO appointmentDTO2 = new AppointmentDTO(LocalDateTime.parse("2008-04-24T10:00:00"), LocalDateTime.parse("2008-04-24T10:00:00"), "e2", new PhysicianAppointmentDTO(), new NurseAppointmentDTO(), new PatientAppointmentDTO());

        Mockito.when(appointmentRepository.findAll()).thenReturn(appointments);
        Mockito.when(appointmentMapper.toDto(appointment1)).thenReturn(appointmentDTO1);
        Mockito.when(appointmentMapper.toDto(appointment2)).thenReturn(appointmentDTO2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found all appointments"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void fetchAllAppointments_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Mockito.when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment")) // change to actual path
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No Appointments found!",
                        result.getResolvedException().getMessage()));
    }
}
