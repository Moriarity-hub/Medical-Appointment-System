package com.example.mas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 预约响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private com.example.mas.dto.response.PatientResponse patient;
    private com.example.mas.dto.response.DoctorResponse doctor;
    private LocalDateTime appointmentDate;
    private String status;
    private String symptoms;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}