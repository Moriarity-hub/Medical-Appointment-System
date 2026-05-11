package com.example.mas.controller;

import com.example.mas.dto.request.AppointmentCreateRequest;
import com.example.mas.dto.request.AppointmentStatusRequest;
import com.example.mas.dto.response.ApiResponse;
import com.example.mas.dto.response.AppointmentResponse;
import com.example.mas.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约控制器
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "预约管理", description = "预约相关接口")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "创建预约", description = "创建新的预约记录")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Valid @RequestBody AppointmentCreateRequest request) {
        AppointmentResponse response = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("预约创建成功", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询预约详情", description = "根据ID查询预约信息")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable Long id) {
        AppointmentResponse response = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "查询所有预约", description = "获取所有预约列表")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
        List<AppointmentResponse> response = appointmentService.getAllAppointments();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "按患者查询预约", description = "根据患者ID查询预约列表")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByPatient(
            @PathVariable Long patientId) {
        List<AppointmentResponse> response = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "按医生查询预约", description = "根据医生ID查询预约列表")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDoctor(
            @PathVariable Long doctorId) {
        List<AppointmentResponse> response = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/date-range")
    @Operation(summary = "按日期范围查询预约", description = "根据日期范围查询预约列表")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AppointmentResponse> response = appointmentService.getAppointmentsByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新预约状态", description = "更新预约的状态")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestBody AppointmentStatusRequest request) {
        AppointmentResponse response = appointmentService.updateAppointmentStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("预约状态更新成功", response));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消预约", description = "取消指定的预约")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("预约已取消", null));
    }
}