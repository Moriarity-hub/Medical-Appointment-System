package com.example.mas.service;

import com.example.mas.dto.request.DoctorCreateRequest;
import com.example.mas.dto.response.DoctorResponse;

import java.util.List;

/**
 * 医生服务接口
 */
public interface DoctorService {
    DoctorResponse createDoctor(DoctorCreateRequest request);
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getAllDoctors();
    List<DoctorResponse> getDoctorsByDepartment(Long departmentId);
    DoctorResponse updateDoctor(Long id, DoctorCreateRequest request);
    void deleteDoctor(Long id);
}