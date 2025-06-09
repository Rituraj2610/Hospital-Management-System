package com.capgemini.hospital_management_system.appointment_tests;

import com.capgemini.hospital_management_system.controller.AppointmentController;
import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.PatientListMapper;
import com.capgemini.hospital_management_system.mapper.PatientMapper;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientMapper patientMapper;

    @MockitoBean
    private PatientListMapper patientListMapper;

    //get dates by patient id
    @Test
    @DisplayName("Get appointment dates by patient ID")
    void getDatesByPatientId_ReturnsAppointmentDates() throws Exception {
        Appointment appointment = new Appointment(1, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2023-06-01T09:00"), LocalDateTime.parse("2023-06-01T09:30"), "room1", new HashSet<>());
        Mockito.when(appointmentRepository.findByPatient_ssn(1)).thenReturn(List.of(appointment));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/date/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("dates recived successfully"))
                .andExpect(jsonPath("$.data[0].appointmentDate").value("2023-06-01T09:00:00"));
    }

    @Test
    @DisplayName("Get appointment dates by patient ID - Not Found")
    void getDatesByPatientId_WhenNotFound_ThrowsException() throws Exception {
        Mockito.when(appointmentRepository.findByPatient_ssn(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/date/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found for patient id 1", result.getResolvedException().getMessage()));
    }
    //get patient by physician id
    @Test
    @DisplayName("Get patients by physician ID")
    void getPatientsByPhysicianId_ReturnsPatients() throws Exception {
        Appointment appointment = new Appointment();
        Mockito.when(appointmentRepository.findByPhysician_employeeId(10)).thenReturn(List.of(appointment));
        Mockito.when(patientListMapper.appointmentToPatientList(Mockito.anyList())).thenReturn(List.of(new PatientAppointmentDTO()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("patient recived successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Get patients by physician ID - Not Found")
    void getPatientsByPhysicianId_NotFound() throws Exception {
        Mockito.when(appointmentRepository.findByPhysician_employeeId(10)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient/10"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No patient found with physician id 10", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patients by physician ID and date")
    void getPatientsByPhysicianIdAndDate_ReturnsPatients() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        Appointment appointment = new Appointment();
        Mockito.when(appointmentRepository.findByPhysician_employeeIdAndStart(10, date)).thenReturn(List.of(appointment));
        Mockito.when(patientListMapper.appointmentToPatientList(Mockito.anyList())).thenReturn(List.of(new PatientAppointmentDTO()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient/10/2023-06-01T09:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("patient recived successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Get patients by physician ID and date - Not Found")
    void getPatientsByPhysicianIdAndDate_NotFound() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        Mockito.when(appointmentRepository.findByPhysician_employeeIdAndStart(10, date)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient/10/2023-06-01T09:00"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No patient found with physician id 10 and appointment date 2023-06-01T09:00", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patient by physicianId and patientId")
    void getPatientByPhysicianIdAndPatientId_ReturnsPatient() throws Exception {
        Patient patient = new Patient();
        Mockito.when(appointmentRepository.findByPhysicianIdAndPatientId(10, 100)).thenReturn(Optional.of(patient));
        Mockito.when(patientMapper.toDto(patient)).thenReturn(new PatientAppointmentDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient")
                        .param("physicianId", "10")
                        .param("patientId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("patient recived successfully"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("Get patient by physicianId and patientId - Not Found")
    void getPatientByPhysicianIdAndPatientId_NotFound() throws Exception {
        Mockito.when(appointmentRepository.findByPhysicianIdAndPatientId(10, 100)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient")
                        .param("physicianId", "10")
                        .param("patientId", "100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found for patient id 100 and physician id 10", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patients by nurse ID")
    void getPatientsByNurseId_ReturnsPatients() throws Exception {
        Mockito.when(appointmentRepository.findByPrepNurse_employeeId(5)).thenReturn(List.of(new Appointment()));
        Mockito.when(patientListMapper.appointmentToPatientList(Mockito.anyList())).thenReturn(List.of(new PatientAppointmentDTO()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient/by-nurse").param("nurseId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("patient recived successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Get patients by nurse ID - Not Found")
    void getPatientsByNurseId_NotFound() throws Exception {
        Mockito.when(appointmentRepository.findByPrepNurse_employeeId(5)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient/by-nurse").param("nurseId", "5"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No patient found with Nurse id 5", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patient by nurse ID and patient ID")
    void getPatientByNurseIdAndPatientId_ReturnsPatient() throws Exception {
        Patient patient = new Patient();
        Mockito.when(appointmentRepository.findByNurseIdAndPatientId(5, 100)).thenReturn(Optional.of(patient));
        Mockito.when(patientMapper.toDto(patient)).thenReturn(new PatientAppointmentDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient/by-nurse-and-patient")
                        .param("NurseId", "5")
                        .param("patientId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("patient recived successfully"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("Get patient by nurse ID and patient ID - Not Found")
    void getPatientByNurseIdAndPatientId_NotFound() throws Exception {
        Mockito.when(appointmentRepository.findByNurseIdAndPatientId(5, 100)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient/by-nurse-and-patient")
                        .param("NurseId", "5")
                        .param("patientId", "100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found for patient id 100 and Nurse id 5", result.getResolvedException().getMessage()));
    }



}