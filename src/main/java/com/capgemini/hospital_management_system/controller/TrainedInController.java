package com.capgemini.hospital_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.hospital_management_system.dto.Response;
import com.capgemini.hospital_management_system.dto.TrainedInDto;
import com.capgemini.hospital_management_system.repository.TrainedInRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TrainedInController {
	
}
