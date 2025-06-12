package com.capgemini.hospital_management_system.appointment_tests;

import com.capgemini.hospital_management_system.controller.AppointmentController;
import com.capgemini.hospital_management_system.dto.*;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.*;
import com.capgemini.hospital_management_system.model.*;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.*;

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
    private PatientMapper patientMapper;

    @MockitoBean
    private PatientListMapper patientListMapper;

    @MockitoBean
    private AppointmentMapper appointmentMapper;

    @MockitoBean
    private PhysicianMapper physicianMapper;

    @MockitoBean
    private NurseMapper nurseCustomMapper;

    @Test
    @DisplayName("Get all appointments with pagination")
    void fetchAllAppointments_ReturnsPaginatedAppointments() throws Exception {
        // Given
        Appointment appointment1 = new Appointment(111, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());
        Appointment appointment2 = new Appointment(222, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e2", new HashSet<>());
        List<Appointment> appointments = List.of(appointment1, appointment2);

        AppointmentDTO appointmentDTO1 = new AppointmentDTO(LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new PhysicianAppointmentDTO(), new NurseAppointmentDTO(), new PatientAppointmentDTO());
        AppointmentDTO appointmentDTO2 = new AppointmentDTO(LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e2", new PhysicianAppointmentDTO(), new NurseAppointmentDTO(), new PatientAppointmentDTO());

        Page<Appointment> appointmentPage = new PageImpl<>(appointments, PageRequest.of(0, 10, Sort.by("id")), appointments.size());

        Mockito.when(appointmentRepository.findAll(Mockito.any(Pageable.class))).thenReturn(appointmentPage);
        Mockito.when(appointmentMapper.toDto(appointment1)).thenReturn(appointmentDTO1);
        Mockito.when(appointmentMapper.toDto(appointment2)).thenReturn(appointmentDTO2);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments?page=0&size=10&sort=id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found all appointments"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].start").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data.content[0].end").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data.content[0].examinationRoom").value("e1"))
                .andExpect(jsonPath("$.data.content[1].start").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data.content[1].end").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data.content[1].examinationRoom").value("e2"))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get all appointments throws error when empty")
    void fetchAllAppointments_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findAll(Mockito.any(Pageable.class))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No Appointments found!", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get appointments by start date with pagination")
    void fetchAppointmentsByStartBy_ReturnsPaginatedAppointments() throws Exception {
        // Given
        Appointment appointment1 = new Appointment(111, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());
        List<Appointment> appointments = List.of(appointment1);

        AppointmentDTO appointmentDTO1 = new AppointmentDTO(LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new PhysicianAppointmentDTO(), new NurseAppointmentDTO(), new PatientAppointmentDTO());

        Page<Appointment> appointmentPage = new PageImpl<>(appointments, PageRequest.of(0, 10, Sort.by("id")), appointments.size());

        Mockito.when(appointmentRepository.findByStart(Mockito.eq(LocalDateTime.parse("2008-04-24T10:00")), Mockito.any(Pageable.class)))
                .thenReturn(appointmentPage);
        Mockito.when(appointmentMapper.toDto(appointment1)).thenReturn(appointmentDTO1);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/2008-04-24T10:00?page=0&size=10&sort=id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found all appointments"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].start").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data.content[0].end").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data.content[0].examinationRoom").value("e1"))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get appointments by start date throws error when empty")
    void fetchAppointmentsByStartBy_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findByStart(Mockito.eq(LocalDateTime.parse("2008-04-24T10:00")), Mockito.any(Pageable.class)))
                .thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/2008-04-24T10:00?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointments found!", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patient by appointment ID")
    void fetchPatientFromAppointmentId_ReturnsPatientDto() throws Exception {
        // Given
        Patient patient = new Patient(123, "John", "abc", "1234556", 12345, new Physician(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        PatientAppointmentDTO patientDTO = new PatientAppointmentDTO("John", "abc", "1234556", 12345, 111);

        Mockito.when(appointmentRepository.findPatientByAppointmentId(111)).thenReturn(Optional.of(patient));
        Mockito.when(patientMapper.toDto(patient)).thenReturn(patientDTO);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the patient's appointment"))
                .andExpect(jsonPath("$.data.name").value("John"))
                .andExpect(jsonPath("$.data.address").value("abc"))
                .andExpect(jsonPath("$.data.phone").value("1234556"))
                .andExpect(jsonPath("$.data.insuranceId").value(12345));
    }

    @Test
    @DisplayName("Get physician by appointment ID")
    void fetchPhysicianFromAppointmentId_ReturnsPhysicianDto() throws Exception {
        // Given
        Physician physician = new Physician(111, "p1", "nice", 1111, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        PhysicianAppointmentDTO physicianAppointmentDTO = new PhysicianAppointmentDTO(111, "p1", "nice", 1111);

        Mockito.when(appointmentRepository.findPhysicianByAppointmentId(111)).thenReturn(Optional.of(physician));
        Mockito.when(physicianMapper.toDto(physician)).thenReturn(physicianAppointmentDTO);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/physician/111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the physician's appointment"))
                .andExpect(jsonPath("$.data.employeeId").value(111))
                .andExpect(jsonPath("$.data.name").value("p1"))
                .andExpect(jsonPath("$.data.position").value("nice"))
                .andExpect(jsonPath("$.data.ssn").value(1111));
    }

    @Test
    @DisplayName("Get nurse by appointment ID")
    void fetchNurseFromAppointmentId_ReturnsNurseDto() throws Exception {
        // Given
        Nurse nurse = new Nurse(111, "n1", "head", true, 1111,
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        NurseAppointmentDTO nurseAppointmentDTO = new NurseAppointmentDTO(111, "n1", "head", true, 1111);

        Mockito.when(appointmentRepository.fetchNurseByAppointmentId(111)).thenReturn(Optional.of(nurse));
        Mockito.when(nurseCustomMapper.toDto(nurse)).thenReturn(nurseAppointmentDTO);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/nurse/111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the nurse's appointment"))
                .andExpect(jsonPath("$.data.name").value("n1"))
                .andExpect(jsonPath("$.data.position").value("head"))
                .andExpect(jsonPath("$.data.registered").value(true))
                .andExpect(jsonPath("$.data.ssn").value(1111));
    }

    @Test
    @DisplayName("Get room by appointment ID")
    void fetchRoomFromAppointmentId_ReturnsStringRoom() throws Exception {
        // Given
        Appointment appointment1 = new Appointment(111, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());

        Mockito.when(appointmentRepository.findExaminationRoomByAppointmentId(111)).thenReturn(appointment1.getExaminationRoom());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/examinationroom/111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the room"))
                .andExpect(jsonPath("$.data").value("e1"));
    }

    @Test
    @DisplayName("Get physician by patient ID with pagination")
    void fetchPhysicianFromPatientId_ReturnsPaginatedPhysicians() throws Exception {
        // Given
        List<Appointment> appointments = getAppointments();
        PhysicianAppointmentDTO physicianAppointmentDTO = new PhysicianAppointmentDTO(111, "p1", "nice", 1111);

        Page<Appointment> appointmentPage = new PageImpl<>(appointments, PageRequest.of(0, 10, Sort.by("id")), appointments.size());

        Mockito.when(appointmentRepository.findByPatient_Ssn(Mockito.eq(222), Mockito.any(Pageable.class))).thenReturn(appointmentPage);
        Mockito.when(physicianMapper.toDto(Mockito.any(Physician.class))).thenReturn(physicianAppointmentDTO);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/physician/patient/222?page=0&size=10&sort=id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the physicians"))
                .andExpect(jsonPath("$.data.content[0].employeeId").value(111))
                .andExpect(jsonPath("$.data.content[0].name").value("p1"))
                .andExpect(jsonPath("$.data.content[0].position").value("nice"))
                .andExpect(jsonPath("$.data.content[0].ssn").value(1111))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get physician by patient ID throws error when empty")
    void fetchPhysicianFromPatientId_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findByPatient_Ssn(Mockito.eq(222), Mockito.any(Pageable.class))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/physician/patient/222?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found for the given patient", result.getResolvedException().getMessage()));
    }

    private static List<Appointment> getAppointments() {
        Physician physician = new Physician(111, "p1", "nice", 1111, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Patient patient = new Patient(123, "John", "abc", "1234556", 12345, new Physician(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Appointment appointment1 = new Appointment(111, patient, new Nurse(), physician, LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());
        return List.of(appointment1);
    }

    @Test
    @DisplayName("Get physician by patient ID and date")
    void fetchPhysicianByPatientIdByStartDate_ReturnsPhysician() throws Exception {
        // Given
        Physician physician = new Physician(111, "p1", "nice", 1111, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Appointment appointment = new Appointment(111, new Patient(), new Nurse(), physician, LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());
        PhysicianAppointmentDTO physicianAppointmentDTO = new PhysicianAppointmentDTO(111, "p1", "nice", 1111);

        Mockito.when(appointmentRepository.findByStartAndPatient_Ssn(LocalDateTime.parse("2008-04-24T10:00"), 111))
                .thenReturn(Optional.of(appointment));
        Mockito.when(physicianMapper.toDto(physician)).thenReturn(physicianAppointmentDTO);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/physician/111/2008-04-24T10:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the physician"))
                .andExpect(jsonPath("$.data.employeeId").value(111))
                .andExpect(jsonPath("$.data.name").value("p1"))
                .andExpect(jsonPath("$.data.position").value("nice"))
                .andExpect(jsonPath("$.data.ssn").value(1111));
    }

    @Test
    @DisplayName("Get physician by patient ID and date throws error")
    void fetchPhysicianByPatientIdByStartDate_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Mockito.when(appointmentRepository.findByStartAndPatient_Ssn(LocalDateTime.parse("2008-04-24T10:00"), 111))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/physician/111/2008-04-24T10:00"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get nurse by patient ID with pagination")
    void fetchNurseFromPatientId_ReturnsPaginatedNurses() throws Exception {
        // Given
        Nurse nurse = new Nurse(111, "n1", "head", true, 1111, new HashSet<>(), new HashSet<>(), new HashSet<>());
        Appointment appointment1 = new Appointment(111, new Patient(), nurse, new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());
        List<Appointment> appointments = List.of(appointment1);
        NurseAppointmentDTO nurseAppointmentDTO = new NurseAppointmentDTO(111, "n1", "head", true, 1111);

        Page<Appointment> appointmentPage = new PageImpl<>(appointments, PageRequest.of(0, 10, Sort.by("id")), appointments.size());

        Mockito.when(appointmentRepository.findByPatient_Ssn(Mockito.eq(222), Mockito.any(Pageable.class))).thenReturn(appointmentPage);
        Mockito.when(nurseCustomMapper.toDto(nurse)).thenReturn(nurseAppointmentDTO);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/nurse/patient/222?page=0&size=10&sort=id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the nurses"))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].name").value("n1"))
                .andExpect(jsonPath("$.data.content[0].position").value("head"))
                .andExpect(jsonPath("$.data.content[0].registered").value(true))
                .andExpect(jsonPath("$.data.content[0].ssn").value(1111))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get nurse by patient ID throws error when empty")
    void fetchNurseFromPatientId_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findByPatient_Ssn(Mockito.eq(222), Mockito.any(Pageable.class))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/nurse/patient/222?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointments found", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get nurse by patient ID and date")
    void fetchNurseByPatientIdByStartDate_ReturnsNurse() throws Exception {
        // Given
        Nurse nurse = new Nurse(111, "n1", "head", true, 1111, new HashSet<>(), new HashSet<>(), new HashSet<>());
        Appointment appointment = new Appointment(111, new Patient(), nurse, new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());
        NurseAppointmentDTO nurseAppointmentDTO = new NurseAppointmentDTO(111, "n1", "head", true, 1111);

        Mockito.when(appointmentRepository.findByStartAndPatient_Ssn(LocalDateTime.parse("2008-04-24T10:00"), 111))
                .thenReturn(Optional.of(appointment));
        Mockito.when(nurseCustomMapper.toDto(nurse)).thenReturn(nurseAppointmentDTO);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/nurse/111/2008-04-24T10:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the nurse"))
                .andExpect(jsonPath("$.data.name").value("n1"))
                .andExpect(jsonPath("$.data.position").value("head"))
                .andExpect(jsonPath("$.data.registered").value(true))
                .andExpect(jsonPath("$.data.ssn").value(1111));
    }

    @Test
    @DisplayName("Get nurse by patient ID and date throws error")
    void fetchNurseByPatientIdByStartDate_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Mockito.when(appointmentRepository.findByStartAndPatient_Ssn(LocalDateTime.parse("2008-04-24T10:00"), 111))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/nurse/111/2008-04-24T10:00"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get appointment dates by patient ID with pagination")
    void getDatesByPatientId_ReturnsPaginatedAppointmentDates() throws Exception {
        // Given
        Appointment appointment = new Appointment(1, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2023-06-01T09:00"), LocalDateTime.parse("2023-06-01T09:30"), "room1", new HashSet<>());
        List<Appointment> appointments = List.of(appointment);
        Page<Appointment> appointmentPage = new PageImpl<>(appointments, PageRequest.of(0, 10, Sort.by("start")), appointments.size());

        AppointmentDatesDTO appointmentDatesDTO = new AppointmentDatesDTO(LocalDateTime.parse("2023-06-01T09:00"));

        Mockito.when(appointmentRepository.findByPatient_Ssn(Mockito.eq(1), Mockito.any(Pageable.class))).thenReturn(appointmentPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/date/1?page=0&size=10&sort=start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Dates received successfully"))
                .andExpect(jsonPath("$.data.content[0].appointmentDate").value("2023-06-01T09:00:00"))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get appointment dates by patient ID throws error when empty")
    void getDatesByPatientId_WhenNotFound_ThrowsException() throws Exception {
        // Given
        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("start")), 0);
        Mockito.when(appointmentRepository.findByPatient_Ssn(Mockito.eq(1), Mockito.any(Pageable.class))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/date/1?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found for patient id 1", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patients by physician ID with pagination")
    void getPatientsByPhysicianId_ReturnsPaginatedPatients() throws Exception {
        // Given
        Appointment appointment = new Appointment();
        List<Appointment> appointments = List.of(appointment);
        Page<Appointment> appointmentPage = new PageImpl<>(appointments, PageRequest.of(0, 10, Sort.by("id")), appointments.size());

        Mockito.when(appointmentRepository.findByPhysician_employeeId(Mockito.eq(10), Mockito.any(Pageable.class))).thenReturn(appointmentPage);
        Mockito.when(patientListMapper.appointmentToPatientList(appointments)).thenReturn(List.of(new PatientAppointmentDTO()));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/physician/10?page=0&size=10&sort=id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Patients received successfully"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get patients by physician ID throws error when empty")
    void getPatientsByPhysicianId_WhenNotFound_ThrowsException() throws Exception {
        // Given
        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findByPhysician_employeeId(Mockito.eq(10), Mockito.any(Pageable.class))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/physician/10?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No patient found with physician id 10", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patients by physician ID and date with pagination")
    void getPatientsByPhysicianIdAndDate_ReturnsPaginatedPatients() throws Exception {
        // Given
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        Appointment appointment = new Appointment();
        List<Appointment> appointments = List.of(appointment);
        Page<Appointment> appointmentPage = new PageImpl<>(appointments, PageRequest.of(0, 10, Sort.by("id")), appointments.size());

        Mockito.when(appointmentRepository.findByPhysician_employeeIdAndStart(Mockito.eq(10), Mockito.eq(date), Mockito.any(Pageable.class)))
                .thenReturn(appointmentPage);
        Mockito.when(patientListMapper.appointmentToPatientList(appointments)).thenReturn(List.of(new PatientAppointmentDTO()));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/10/2023-06-01T09:00?page=0&size=10&sort=id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Patients received successfully"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get patients by physician ID and date throws error when empty")
    void getPatientsByPhysicianIdAndDate_WhenNotFound_ThrowsException() throws Exception {
        // Given
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findByPhysician_employeeIdAndStart(Mockito.eq(10), Mockito.eq(date), Mockito.any(Pageable.class)))
                .thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/10/2023-06-01T09:00?page=0&size=10"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No patient found with physician id 10 and appointment date 2023-06-01T09:00", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patient by physician ID and patient ID")
    void getPatientByPhysicianIdAndPatientId_ReturnsPatient() throws Exception {
        // Given
        Patient patient = new Patient();
        Mockito.when(appointmentRepository.findByPhysicianIdAndPatientId(10, 100)).thenReturn(Optional.of(patient));
        Mockito.when(patientMapper.toDto(patient)).thenReturn(new PatientAppointmentDTO());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient")
                        .param("physicianId", "10")
                        .param("patientId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Patient received successfully"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("Get patient by physician ID and patient ID throws error when not found")
    void getPatientByPhysicianIdAndPatientId_WhenNotFound_ThrowsException() throws Exception {
        // Given
        Mockito.when(appointmentRepository.findByPhysicianIdAndPatientId(10, 100)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient")
                        .param("physicianId", "10")
                        .param("patientId", "100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found for patient id 100 and physician id 10", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patients by nurse ID with pagination")
    void getPatientsByNurseId_ReturnsPaginatedPatients() throws Exception {
        // Given
        Appointment appointment = new Appointment();
        List<Appointment> appointments = List.of(appointment);
        Page<Appointment> appointmentPage = new PageImpl<>(appointments, PageRequest.of(0, 10, Sort.by("id")), appointments.size());

        Mockito.when(appointmentRepository.findByPrepNurse_employeeId(Mockito.eq(5), Mockito.any(Pageable.class))).thenReturn(appointmentPage);
        Mockito.when(patientListMapper.appointmentToPatientList(appointments)).thenReturn(List.of(new PatientAppointmentDTO()));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/by-nurse?page=0&size=10&sort=id")
                        .param("nurseId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Patients received successfully"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get patients by nurse ID throws error when empty")
    void getPatientsByNurseId_WhenNotFound_ThrowsException() throws Exception {
        // Given
        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findByPrepNurse_employeeId(Mockito.eq(5), Mockito.any(Pageable.class))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/by-nurse?page=0&size=10")
                        .param("nurseId", "5"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No patient found with Nurse id 5", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patient by nurse ID and patient ID")
    void getPatientByNurseIdAndPatientId_ReturnsPatient() throws Exception {
        // Given
        Patient patient = new Patient();
        Mockito.when(appointmentRepository.findByNurseIdAndPatientId(5, 100)).thenReturn(Optional.of(patient));
        Mockito.when(patientMapper.toDto(patient)).thenReturn(new PatientAppointmentDTO());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/by-nurse-and-patient")
                        .param("NurseId", "5")
                        .param("patientId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Patient received successfully"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("Get patient by nurse ID and patient ID throws error when not found")
    void getPatientByNurseIdAndPatientId_WhenNotFound_ThrowsException() throws Exception {
        // Given
        Mockito.when(appointmentRepository.findByNurseIdAndPatientId(5, 100)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/by-nurse-and-patient")
                        .param("NurseId", "5")
                        .param("patientId", "100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found for patient id 100 and Nurse id 5", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patients by nurse ID and date with pagination")
    void getPatientsByNurseIdAndDate_ReturnsPaginatedPatients() throws Exception {
        // Given
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        Appointment appointment = new Appointment();
        List<Appointment> appointments = List.of(appointment);
        Page<Appointment> appointmentPage = new PageImpl<>(appointments, PageRequest.of(0, 10, Sort.by("id")), appointments.size());

        Mockito.when(appointmentRepository.findByPrepNurse_employeeIdAndStart(Mockito.eq(1), Mockito.eq(date), Mockito.any(Pageable.class)))
                .thenReturn(appointmentPage);
        Mockito.when(patientListMapper.appointmentToPatientList(appointments)).thenReturn(List.of(new PatientAppointmentDTO()));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/by-nurse-and-date?page=0&size=10&sort=id")
                        .param("nurseId", "1")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Patients received successfully"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get patients by nurse ID and date throws error when empty")
    void getPatientsByNurseIdAndDate_WhenNotFound_ThrowsException() throws Exception {
        // Given
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        Page<Appointment> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findByPrepNurse_employeeIdAndStart(Mockito.eq(1), Mockito.eq(date), Mockito.any(Pageable.class)))
                .thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/patient/by-nurse-and-date?page=0&size=10")
                        .param("nurseId", "1")
                        .param("date", date.toString()))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No patient found with nurseId 1 and appointment date 2023-06-01T09:00", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get room by patient ID and date")
    void getRoomByPatientIdAndDate_ReturnsRoom() throws Exception {
        // Given
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        Mockito.when(appointmentRepository.findByPatientIdAndStartDate(2, date)).thenReturn(Optional.of("room1"));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/room/by-patient-and-date")
                        .param("patientId", "2")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Room received successfully"))
                .andExpect(jsonPath("$.data").value("room1"));
    }

    @Test
    @DisplayName("Get rooms by physician ID and date with pagination")
    void getRoomsByPhysicianIdAndDate_ReturnsPaginatedRooms() throws Exception {
        // Given
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        List<String> rooms = List.of("room1", "room2");
        Page<String> roomPage = new PageImpl<>(rooms, PageRequest.of(0, 10, Sort.by("id")), rooms.size());

        Mockito.when(appointmentRepository.findByPhysicianIdAndStartDate(Mockito.eq(3), Mockito.eq(date), Mockito.any(Pageable.class)))
                .thenReturn(roomPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/rooms/by-physician-and-date?page=0&size=10&sort=id")
                        .param("physicianId", "3")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Rooms received successfully"))
                .andExpect(jsonPath("$.data.content[0]").value("room1"))
                .andExpect(jsonPath("$.data.content[1]").value("room2"))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get rooms by physician ID and date throws error when empty")
    void getRoomsByPhysicianIdAndDate_WhenNotFound_ThrowsException() throws Exception {
        // Given
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        Page<String> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findByPhysicianIdAndStartDate(Mockito.eq(3), Mockito.eq(date), Mockito.any(Pageable.class)))
                .thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/rooms/by-physician-and-date?page=0&size=10")
                        .param("physicianId", "3")
                        .param("date", date.toString()))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No room found with physicianId 3 and appointment date 2023-06-01T09:00", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get rooms by nurse ID and date with pagination")
    void getRoomsByNurseIdAndDate_ReturnsPaginatedRooms() throws Exception {
        // Given
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        List<String> rooms = List.of("roomA");
        Page<String> roomPage = new PageImpl<>(rooms, PageRequest.of(0, 10, Sort.by("id")), rooms.size());

        Mockito.when(appointmentRepository.findByNurseIdAndStartDate(Mockito.eq(1), Mockito.eq(date), Mockito.any(Pageable.class)))
                .thenReturn(roomPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/rooms/by-nurse-and-date?page=0&size=10&sort=id")
                        .param("nurseId", "1")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Rooms received successfully"))
                .andExpect(jsonPath("$.data.content[0]").value("roomA"))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    @DisplayName("Get rooms by nurse ID and date throws error when empty")
    void getRoomsByNurseIdAndDate_WhenNotFound_ThrowsException() throws Exception {
        // Given
        LocalDateTime date = LocalDateTime.parse("2023-06-01T09:00");
        Page<String> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10, Sort.by("id")), 0);
        Mockito.when(appointmentRepository.findByNurseIdAndStartDate(Mockito.eq(1), Mockito.eq(date), Mockito.any(Pageable.class)))
                .thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/rooms/by-nurse-and-date?page=0&size=10")
                        .param("nurseId", "1")
                        .param("date", date.toString()))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No room found with nurseId 1 and appointment date 2023-06-01T09:00", result.getResolvedException().getMessage()));
    }
}