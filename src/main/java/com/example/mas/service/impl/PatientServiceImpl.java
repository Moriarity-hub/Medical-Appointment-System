package com.example.mas.service.impl;

import com.example.mas.dto.request.PatientCreateRequest;
import com.example.mas.dto.response.PatientResponse;
import com.example.mas.entity.Patient;
import com.example.mas.exception.BusinessException;
import com.example.mas.repository.PatientRepository;
import com.example.mas.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 患者服务实现类
 */
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public PatientResponse createPatient(PatientCreateRequest request) {

        // 检查身份证是否已存在
        if (request.getIdCard() != null &&
                patientRepository.findByIdCard(request.getIdCard()).isPresent()) {
            throw new BusinessException("身份证号码已被注册");
        }

        // 检查手机号是否已存在
        if (patientRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new BusinessException("手机号码已被注册");
        }

        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setIdCard(request.getIdCard());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());

        // 如果设置了密码，则加密存储
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            patient.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        patient = patientRepository.save(patient);
        return toResponse(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new BusinessException("患者不存在"));
        return toResponse(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, PatientCreateRequest request) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new BusinessException("患者不存在"));

        // 检查身份证是否被其他患者使用
        if (request.getIdCard() != null &&
                !request.getIdCard().equals(patient.getIdCard()) &&
                patientRepository.findByIdCard(request.getIdCard()).isPresent()) {
            throw new BusinessException("身份证号码已被注册");
        }

        // 检查手机号是否被其他患者使用
        if (!request.getPhone().equals(patient.getPhone()) &&
                patientRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new BusinessException("手机号码已被注册");
        }

        patient.setName(request.getName());
        patient.setIdCard(request.getIdCard());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());

        // 如果设置了密码，则加密存储
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            patient.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        patient = patientRepository.save(patient);
        return toResponse(patient);
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new BusinessException("患者不存在");
        }
        patientRepository.deleteById(id);
    }

    private PatientResponse toResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setName(patient.getName());
        response.setIdCard(patient.getIdCard());
        response.setPhone(patient.getPhone());
        response.setEmail(patient.getEmail());
        response.setAddress(patient.getAddress());
        response.setCreatedAt(patient.getCreatedAt());
        response.setUpdatedAt(patient.getUpdatedAt());
        return response;
    }

}