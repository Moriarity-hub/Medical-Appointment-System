// D:\maven_project\mas\src\main\java\com\example\mas\service\impl\AuthServiceImpl.java
package com.example.mas.service.impl;

import com.example.mas.config.JwtUtil;
import com.example.mas.dto.request.LoginRequest;
import com.example.mas.dto.request.RegisterRequest;
import com.example.mas.dto.response.LoginResponse;
import com.example.mas.entity.Patient;
import com.example.mas.exception.BusinessException;
import com.example.mas.repository.PatientRepository;
import com.example.mas.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PatientRepository patientRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Patient patient = patientRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new BusinessException("手机号或密码错误"));

        // 如果患者没有设置密码，默认使用手机号后6位作为初始密码
        if (patient.getPassword() == null || patient.getPassword().isEmpty()) {
            String defaultPassword = request.getPhone().length() >= 6
                    ? request.getPhone().substring(request.getPhone().length() - 6)
                    : request.getPhone();
            if (!request.getPassword().equals(defaultPassword)) {
                throw new BusinessException("手机号或密码错误");
            }
        } else {
            if (!passwordEncoder.matches(request.getPassword(), patient.getPassword())) {
                throw new BusinessException("手机号或密码错误");
            }
        }

        String token = jwtUtil.generateToken(patient.getPhone(), patient.getId());

        return new LoginResponse(token, "Bearer", patient.getId(), patient.getName(), patient.getPhone());
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // 检查手机号是否已被注册
        if (patientRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("该手机号已被注册");
        }

        // 检查身份证号是否已被注册
        if (request.getIdCard() != null && !request.getIdCard().isEmpty()) {
            if (patientRepository.existsByIdCard(request.getIdCard())) {
                throw new BusinessException("该身份证号已被注册");
            }
        }

        // 创建患者实体
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setIdCard(request.getIdCard());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());

        // 设置密码（如果未设置，使用手机号后6位）
        String password = request.getPassword();
        if (password == null || password.isEmpty()) {
            password = request.getPhone().length() >= 6
                    ? request.getPhone().substring(request.getPhone().length() - 6)
                    : request.getPhone();
        }
        patient.setPassword(passwordEncoder.encode(password));

        // 保存患者
        patient = patientRepository.save(patient);

        // 生成token
        String token = jwtUtil.generateToken(patient.getPhone(), patient.getId());

        return new LoginResponse(token, "Bearer", patient.getId(), patient.getName(), patient.getPhone());
    }
}