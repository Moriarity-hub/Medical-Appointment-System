package com.example.mas.service;

import com.example.mas.dto.request.PatientCreateRequest;
import com.example.mas.dto.response.PatientResponse;

import java.util.List;

/**
 * 患者服务接口
 */
public interface PatientService {
    PatientResponse createPatient(PatientCreateRequest request);
    PatientResponse getPatientById(Long id);
    List<PatientResponse> getAllPatients();
    PatientResponse updatePatient(Long id, PatientCreateRequest request);
    void deletePatient(Long id);
}