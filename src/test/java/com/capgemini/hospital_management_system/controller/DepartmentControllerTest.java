package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.model.Department;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.repository.DepartmentRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    // ✅ Replace @MockBean with this inner test config
    @TestConfiguration
    static class MockConfig {
        @Bean
        public DepartmentRepository departmentRepository() {
            return Mockito.mock(DepartmentRepository.class);
        }
    }

    @Test
    public void testGetAllDepartments() throws Exception {
        // Mock Department
        Department dept = new Department();
        dept.setDepartmentId(1);
        dept.setName("Test Department");

        Physician head = new Physician();
        head.setEmployeeId(100);
        dept.setHead(head);

        when(departmentRepository.findAll()).thenReturn(List.of(dept));

        // Perform GET request and assert
        mockMvc.perform(get("/api/department/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound()) // 302
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].departmentId").value(1))
                .andExpect(jsonPath("$.data[0].headId").value(100));
    }
}
