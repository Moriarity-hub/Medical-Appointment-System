package com.example.mas.service;

import com.example.mas.dto.request.DepartmentCreateRequest;
import com.example.mas.dto.response.DepartmentResponse;

import java.util.List;

/**
 * 科室服务接口
 */
public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentCreateRequest request);
    DepartmentResponse getDepartmentById(Long id);
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse updateDepartment(Long id, DepartmentCreateRequest request);
    void deleteDepartment(Long id);
}