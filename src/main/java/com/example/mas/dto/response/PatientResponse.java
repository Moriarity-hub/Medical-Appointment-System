package com.example.mas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 患者响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

    private Long id;
    private String name;
    private String idCard;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}