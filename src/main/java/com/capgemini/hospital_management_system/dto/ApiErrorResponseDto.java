package com.capgemini.hospital_management_system.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiErrorResponseDto {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
   


}