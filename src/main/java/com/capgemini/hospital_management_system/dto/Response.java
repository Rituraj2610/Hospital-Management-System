package com.capgemini.hospital_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Response<T> {
    private int status;
    private String message;
    private T data;

    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
