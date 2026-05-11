package com.example.mas.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 预约创建请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateRequest {

    @NotNull(message = "患者ID不能为空")
    private Long patientId;

    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    @NotNull(message = "预约时间不能为空")
    @Future(message = "预约时间必须是未来时间")
    private LocalDateTime appointmentDate;

    @Size(max = 500, message = "症状描述长度不能超过500个字符")
    private String symptoms;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String notes;
}