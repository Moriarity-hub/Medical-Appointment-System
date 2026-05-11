package com.example.mas.controller;

import com.example.mas.dto.request.DoctorCreateRequest;
import com.example.mas.dto.response.ApiResponse;
import com.example.mas.dto.response.DoctorResponse;
import com.example.mas.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生控制器
 */
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "医生管理", description = "医生相关接口")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(summary = "创建医生", description = "创建新的医生信息")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(
            @Valid @RequestBody DoctorCreateRequest request) {
        DoctorResponse response = doctorService.createDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("医生创建成功", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询医生详情", description = "根据ID查询医生信息")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        DoctorResponse response = doctorService.getDoctorById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "查询所有医生", description = "获取所有医生列表")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        List<DoctorResponse> response = doctorService.getAllDoctors();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "按科室查询医生", description = "根据科室ID查询医生列表")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getDoctorsByDepartment(
            @PathVariable Long departmentId) {
        List<DoctorResponse> response = doctorService.getDoctorsByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新医生", description = "根据ID更新医生信息")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorCreateRequest request) {
        DoctorResponse response = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(ApiResponse.success("医生信息更新成功", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除医生", description = "根据ID删除医生")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("医生删除成功", null));
    }
}