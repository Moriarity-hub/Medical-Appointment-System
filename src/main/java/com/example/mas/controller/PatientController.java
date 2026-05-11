package com.example.mas.controller;

import com.example.mas.dto.request.PatientCreateRequest;
import com.example.mas.dto.response.ApiResponse;
import com.example.mas.dto.response.PatientResponse;
import com.example.mas.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者控制器
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "患者管理", description = "患者相关接口")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(summary = "创建患者", description = "创建新的患者信息")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(
            @Valid @RequestBody PatientCreateRequest request) {
        PatientResponse response = patientService.createPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("患者创建成功", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询患者详情", description = "根据ID查询患者信息")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable Long id) {
        PatientResponse response = patientService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "查询所有患者", description = "获取所有患者列表")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients() {
        List<PatientResponse> response = patientService.getAllPatients();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新患者", description = "根据ID更新患者信息")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientCreateRequest request) {
        PatientResponse response = patientService.updatePatient(id, request);
        return ResponseEntity.ok(ApiResponse.success("患者信息更新成功", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除患者", description = "根据ID删除患者")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success("患者删除成功", null));
    }
}