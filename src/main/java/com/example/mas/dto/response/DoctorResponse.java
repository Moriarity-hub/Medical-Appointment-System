package com.example.mas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 医生响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long id;
    private String name;
    private String title;
    private String specialty;
    private String phone;
    private String email;
    private Boolean available;
    private com.example.mas.dto.response.DepartmentResponse department;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}