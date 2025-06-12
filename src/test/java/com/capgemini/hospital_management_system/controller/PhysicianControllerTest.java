package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.PhysicianDto;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.PhysicianRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhysicianController.class)
public class PhysicianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhysicianRepository physicianRepository;

    @MockitoBean
    private org.modelmapper.ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // No special setup needed, handled by annotations
    }

    @Test
    void testGetPhysicianByName() throws Exception {
        Physician physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("John Doe");
        physician.setPosition("Cardiologist");
        physician.setSsn(12345);

        PhysicianDto physicianDto = new PhysicianDto(1, "John Doe", "Cardiologist", 12345);

        when(physicianRepository.findByName("John Doe")).thenReturn(Optional.of(physician));
        when(modelMapper.map(physician, PhysicianDto.class)).thenReturn(physicianDto);

        mockMvc.perform(get("/api/physician/name/John Doe"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.status").value(302))
                .andExpect(jsonPath("$.message").value("Physician found successfully!"))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void testGetPhysiciansByPosition() throws Exception {
        Physician physician = new Physician();
        physician.setEmployeeId(2);
        physician.setName("Jane Smith");
        physician.setPosition("Neurologist");
        physician.setSsn(54321);

        PhysicianDto physicianDto = new PhysicianDto(2, "Jane Smith", "Neurologist", 54321);

        List<Physician> physicianList = List.of(physician);

        when(physicianRepository.findAllByPosition("Neurologist")).thenReturn(physicianList);
        when(modelMapper.map(physician, PhysicianDto.class)).thenReturn(physicianDto);

        mockMvc.perform(get("/api/physician/position/Neurologist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Physicians fetched successfully"))
                .andExpect(jsonPath("$.data[0].name").value("Jane Smith"));
    }

    @Test
    void testGetPhysicianByEmpId() throws Exception {
        Physician physician = new Physician();
        physician.setEmployeeId(3);
        physician.setName("Alice Johnson");
        physician.setPosition("Dermatologist");
        physician.setSsn(98765);

        PhysicianDto physicianDto = new PhysicianDto(3, "Alice Johnson", "Dermatologist", 98765);

        when(physicianRepository.findById(3)).thenReturn(Optional.of(physician));
        when(modelMapper.map(physician, PhysicianDto.class)).thenReturn(physicianDto);

        mockMvc.perform(get("/api/physician/empid/3"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.status").value(302))
                .andExpect(jsonPath("$.message").value("Physician fetched successfully"))
                .andExpect(jsonPath("$.data.name").value("Alice Johnson"));
    }

    @Test
    void testAddPhysician() throws Exception {
        PhysicianDto physicianDto = new PhysicianDto(4, "Bob Martin", "Oncologist", 55555);
        Physician physician = new Physician();
        physician.setEmployeeId(4);
        physician.setName("Bob Martin");
        physician.setPosition("Oncologist");
        physician.setSsn(55555);

        when(modelMapper.map(physicianDto, Physician.class)).thenReturn(physician);
        when(physicianRepository.save(any(Physician.class))).thenReturn(physician);
        when(modelMapper.map(physician, PhysicianDto.class)).thenReturn(physicianDto);

        mockMvc.perform(post("/api/physician/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(physicianDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Physician created successfully"))
                .andExpect(jsonPath("$.data.name").value("Bob Martin"));
    }

    @Test
    void testUpdatePhysicianPosition() throws Exception {
        Physician physician = new Physician();
        physician.setEmployeeId(5);
        physician.setName("David Lee");
        physician.setPosition("General");
        physician.setSsn(66666);

        PhysicianDto physicianDto = new PhysicianDto(5, "David Lee", "Orthopedic", 66666);

        when(physicianRepository.findById(5)).thenReturn(Optional.of(physician));
        physician.setPosition("Orthopedic");
        when(physicianRepository.save(any(Physician.class))).thenReturn(physician);
        when(modelMapper.map(physician, PhysicianDto.class)).thenReturn(physicianDto);

        mockMvc.perform(put("/api/physician/update/position/Orthopedic/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Physician position updated successfully"))
                .andExpect(jsonPath("$.data.position").value("Orthopedic"));
    }

    @Test
    void testUpdatePhysicianName() throws Exception {
        Physician physician = new Physician();
        physician.setEmployeeId(6);
        physician.setName("Tom Hardy");
        physician.setPosition("Surgeon");
        physician.setSsn(77777);

        PhysicianDto physicianDto = new PhysicianDto(6, "Thomas Hardy", "Surgeon", 77777);

        when(physicianRepository.findById(6)).thenReturn(Optional.of(physician));
        physician.setName("Thomas Hardy");
        when(physicianRepository.save(any(Physician.class))).thenReturn(physician);
        when(modelMapper.map(physician, PhysicianDto.class)).thenReturn(physicianDto);

        mockMvc.perform(put("/api/physician/update/name/6?newName=Thomas Hardy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Physician name updated successfully"))
                .andExpect(jsonPath("$.data.name").value("Thomas Hardy"));
    }

    @Test
    void testUpdatePhysicianSSN() throws Exception {
        Physician physician = new Physician();
        physician.setEmployeeId(7);
        physician.setName("Emma Stone");
        physician.setPosition("Pediatrician");
        physician.setSsn(88888);

        PhysicianDto physicianDto = new PhysicianDto(7, "Emma Stone", "Pediatrician", 99999);

        when(physicianRepository.findById(7)).thenReturn(Optional.of(physician));
        physician.setSsn(99999);
        when(physicianRepository.save(any(Physician.class))).thenReturn(physician);
        when(modelMapper.map(physician, PhysicianDto.class)).thenReturn(physicianDto);

        mockMvc.perform(put("/api/physician/update/ssn/7?newSsn=99999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Physician SSN updated successfully"))
                .andExpect(jsonPath("$.data.ssn").value(99999));
    }


    @Test
    void testGetPhysicianNamesGroupedByPosition() throws Exception {

        // Mocked data returned by repository (position, name)
        List<Object[]> mockData = List.of(
                new Object[]{"Head Surgeon", "Dr. John"},
                new Object[]{"Head Surgeon", "Dr. Alice"},
                new Object[]{"Resident Doctor", "Dr. Bob"}
        );

        // Mock repository call
        when(physicianRepository.findPositionAndPhysicianNames()).thenReturn(mockData);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/physician/group-by-position"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Physicians grouped by position with names"))

                // Check first group
                .andExpect(jsonPath("$.data[?(@.position == 'Head Surgeon')].physicianNames[0]").value("Dr. John"))
                .andExpect(jsonPath("$.data[?(@.position == 'Head Surgeon')].physicianNames[1]").value("Dr. Alice"))

                // Check second group
                .andExpect(jsonPath("$.data[?(@.position == 'Resident Doctor')].physicianNames[0]").value("Dr. Bob"));
    }

}
