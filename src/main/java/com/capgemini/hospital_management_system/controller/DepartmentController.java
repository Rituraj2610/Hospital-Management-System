package com.capgemini.hospital_management_system.controller;


import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.exception.EntityNotFoundException;
import com.capgemini.hospital_management_system.model.Department;
import com.capgemini.hospital_management_system.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;


    @GetMapping("/")
    public ResponseEntity<Response<List<Department>>> getAllDepartment() {

        List<Department> departmentList = departmentRepository.findAll();

        if (departmentList.isEmpty()) {
            throw new EntityNotFoundException("Not able to get all department details");
        }

        Response<List<Department>> response = Response.<List<Department>>builder()
                .status(HttpStatus.OK.value()) // You can also use FOUND.value() if needed
                .message("Department list fetched successfully")
                .data(departmentList)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response , HttpStatus.FOUND); // returns 200 OK with response body
    }


}
