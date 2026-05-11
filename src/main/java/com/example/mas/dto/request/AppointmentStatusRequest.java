package com.example.mas.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预约状态更新请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatusRequest {

    private String status;
}