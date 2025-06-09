package com.capgemini.hospital_management_system.appointment_tests;

import com.capgemini.hospital_management_system.controller.AppointmentController;
import com.capgemini.hospital_management_system.mapper.PatientListMapper;
import com.capgemini.hospital_management_system.mapper.PatientMapper;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.AppointmentRepository;
import com.capgemini.hospital_management_system.dto.AppointmentDTO;
import com.capgemini.hospital_management_system.dto.NurseAppointmentDTO;
import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.dto.PhysicianAppointmentDTO;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.mapper.*;
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
import java.util.Optional;

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
    @DisplayName("Get all appointments")
    void fetchAllAppointments_ReturnsAppointmentsList() throws Exception {
        Appointment appointment1 = new Appointment(111, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());
        Appointment appointment2 = new Appointment(222, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e2", new HashSet<>());
        List<Appointment> appointments = List.of(appointment1, appointment2);

        AppointmentDTO appointmentDTO1 = new AppointmentDTO(LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new PhysicianAppointmentDTO(), new NurseAppointmentDTO(), new PatientAppointmentDTO());
        AppointmentDTO appointmentDTO2 = new AppointmentDTO(LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e2", new PhysicianAppointmentDTO(), new NurseAppointmentDTO(), new PatientAppointmentDTO());

        Mockito.when(appointmentRepository.findAll()).thenReturn(appointments);
        Mockito.when(appointmentMapper.toDto(appointment1)).thenReturn(appointmentDTO1);
        Mockito.when(appointmentMapper.toDto(appointment2)).thenReturn(appointmentDTO2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found all appointments"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].start").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data[0].end").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data[0].examinationRoom").value("e1"))
                .andExpect(jsonPath("$.data[1].start").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data[1].end").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data[1].examinationRoom").value("e2"));
    }

    @Test
    @DisplayName("Get all appointments throws error")
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

    @Test
    @DisplayName("Get appointments by start date")
    void fetchAppointmentsByStartBy_ReturnsAppointmentsList() throws Exception {
        Appointment appointment1 = new Appointment(111, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());
          List<Appointment> appointments = List.of(appointment1);

        AppointmentDTO appointmentDTO1 = new AppointmentDTO(LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new PhysicianAppointmentDTO(), new NurseAppointmentDTO(), new PatientAppointmentDTO());

        Mockito.when(appointmentRepository.findByStart(LocalDateTime.parse("2008-04-24T10:00")))
                .thenReturn(appointments);
        Mockito.when(appointmentMapper.toDto(appointment1)).thenReturn(appointmentDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/2008-04-24T10:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found all appointments"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].start").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data[0].end").value("2008-04-24T10:00:00"))
                .andExpect(jsonPath("$.data[0].examinationRoom").value("e1"));
                }

    @Test
    @DisplayName("Get appointments by start date throws error")
    void etchAppointmentsByStartBy_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Mockito.when(appointmentRepository.findByStart(LocalDateTime.parse("2008-04-24T10:00"))).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/2008-04-24T10:00")) // change to actual path
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointments found!",
                        result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get patient by appointment ID")
    void fetchPatientFromAppointmentId_ReturnsPatientDto() throws Exception {

        Patient patient = new Patient(123, "John", "abc", "1234556", 12345, new Physician(),
                new HashSet<>(), new HashSet<>(),new HashSet<>(),new HashSet<>());

        PatientAppointmentDTO patientDTO = new PatientAppointmentDTO("John", "abc", "1234556", 12345, 111);

        Mockito.when(appointmentRepository.findPatientByAppointmentId(111)).thenReturn(Optional.of(patient));
        Mockito.when(patientMapper.toDto(patient)).thenReturn(patientDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/patient/111"))
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

        Physician physician = new Physician(111, "p1", "nice", 1111, new HashSet<>(), new HashSet<>(),
                new HashSet<>(),new HashSet<>(),new HashSet<>(),new HashSet<>(),new HashSet<>());

        PhysicianAppointmentDTO physicianAppointmentDTO = new PhysicianAppointmentDTO(111, "p1", "nice", 1111);

        Mockito.when(appointmentRepository.findPhysicianByAppointmentId(111)).thenReturn(Optional.of(physician));
        Mockito.when(physicianMapper.toDto(physician)).thenReturn(physicianAppointmentDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/physician/111"))
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

        Nurse nurse = new Nurse(111, "n1", "head", true, 1111,
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        NurseAppointmentDTO nurseAppointmentDTO = new NurseAppointmentDTO(111, "n1", "head", true, 1111);

        Mockito.when(appointmentRepository.fetchNurseByAppointmentId(111)).thenReturn(Optional.of(nurse));
        Mockito.when(nurseCustomMapper.toDto(nurse)).thenReturn(nurseAppointmentDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/nurse/111"))
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

        Appointment appointment1 = new Appointment(111, new Patient(), new Nurse(), new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());

        Mockito.when(appointmentRepository.findExaminationRoomByAppointmentId(111)).thenReturn(appointment1.getExaminationRoom());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/examinationroom/111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the room"))
                .andExpect(jsonPath("$.data").value("e1"));
    }

    @Test
    @DisplayName("Get physician by patient ID")
    void fetchPhysicianFromPatientId_ReturnsListOfPhysicians() throws Exception {

        List<Appointment> appointments = getAppointments();
        PhysicianAppointmentDTO physicianAppointmentDTO = new PhysicianAppointmentDTO(111, "p1", "nice", 1111);
        List<PhysicianAppointmentDTO> physicianAppointmentDTOS = List.of(physicianAppointmentDTO);

        Mockito.when(appointmentRepository.findByPatient_Ssn(222)).thenReturn(appointments);
        Mockito.when(physicianMapper.toDto(Mockito.any(Physician.class)))
                .thenReturn(new PhysicianAppointmentDTO(111, "p1", "nice", 1111));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/physician/patient/222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the physicians"))
                .andExpect(jsonPath("$.data[0].employeeId").value(111))
                .andExpect(jsonPath("$.data[0].name").value("p1"))
                .andExpect(jsonPath("$.data[0].position").value("nice"))
                .andExpect(jsonPath("$.data[0].ssn").value(1111));
    }

    private static List<Appointment> getAppointments() {
        Physician physician = new Physician(111, "p1", "nice", 1111, new HashSet<>(), new HashSet<>(),
                new HashSet<>(),new HashSet<>(),new HashSet<>(),new HashSet<>(),new HashSet<>());

        Patient patient = new Patient(123, "John", "abc", "1234556", 12345, new Physician(),
                new HashSet<>(), new HashSet<>(),new HashSet<>(),new HashSet<>());

        Appointment appointment1 = new Appointment(111, patient, new Nurse(), physician, LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());
        List<Appointment> appointments = List.of(appointment1);
        return appointments;
    }

    @Test
    @DisplayName("Get physicians by patient id on a date throws error")
    void fetchPhysicianByPatientIdByStartDate_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Mockito.when(appointmentRepository.findByStartAndPatient_Ssn(LocalDateTime.parse("2008-04-24T10:00:00"), 111)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/physician/111/2008-04-24T10:00:00"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found",
                        result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get nurse by patient ID")
    void fetchNurseFromPatientId_ReturnsNurseDto() throws Exception {

        Nurse nurse = new Nurse(111, "n1", "head", true, 1111,
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        Appointment appointment1 = new Appointment(111, new Patient(), nurse, new Physician(), LocalDateTime.parse("2008-04-24T10:00"), LocalDateTime.parse("2008-04-24T10:00"), "e1", new HashSet<>());

        List<Appointment> appointments = List.of(appointment1);


        NurseAppointmentDTO nurseAppointmentDTO = new NurseAppointmentDTO(111, "n1", "head", true, 1111);

        Mockito.when(appointmentRepository.findByPatient_Ssn(222)).thenReturn(appointments);
        Mockito.when(nurseCustomMapper.toDto(Mockito.any(Nurse.class))).thenReturn(nurseAppointmentDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/nurse/patient/222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Found the nurses"))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("n1"))
                .andExpect(jsonPath("$.data[0].position").value("head"))
                .andExpect(jsonPath("$.data[0].registered").value(true))
                .andExpect(jsonPath("$.data[0].ssn").value(1111));
    }

    @Test
    @DisplayName("Get physicians by patient id on a date throws error")
    void fetchNurseByPatientIdByStartDate_WhenNoAppointments_ThrowsEntityNotFoundException() throws Exception {
        // Given
        Mockito.when(appointmentRepository.findByStartAndPatient_Ssn(LocalDateTime.parse("2008-04-24T10:00:00"), 111)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/nurse/111/2008-04-24T10:00:00"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> Assertions.assertEquals("No appointment found",
                        result.getResolvedException().getMessage()));
    }
  
  
  
    // AMAN
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
   
}
