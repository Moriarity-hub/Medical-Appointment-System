// D:\maven_project\mas\src\main\java\com\example\mas\service\impl\AuthServiceImpl.java
package com.example.mas.service.impl;

import com.example.mas.config.JwtUtil;
import com.example.mas.dto.request.LoginRequest;
import com.example.mas.dto.request.RegisterRequest;
import com.example.mas.dto.response.LoginResponse;
import com.example.mas.entity.*;
import com.example.mas.exception.BusinessException;
import com.example.mas.repository.DoctorRepository;
import com.example.mas.repository.PatientRepository;
import com.example.mas.repository.UserRepository;
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

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // 先尝试从 user 表查找
        java.util.Optional<User> userOpt = userRepository.findByUsername(request.getPhone());
        
        if (userOpt.isPresent()) {
            // 用户存在于 user 表
            User user = userOpt.get();
            
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BusinessException("用户名或密码错误");
            }

            String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());

            String userName = "";
            String phone = user.getUsername();

            if (user.getRole() == UserRole.PATIENT) {
                Patient patient = patientRepository.findById(user.getRelatedId()).orElse(null);
                if (patient != null) {
                    userName = patient.getName();
                    phone = patient.getPhone();
                }
            } else if (user.getRole() == UserRole.DOCTOR) {
                Doctor doctor = doctorRepository.findById(user.getRelatedId()).orElse(null);
                if (doctor != null) {
                    userName = doctor.getName();
                    phone = doctor.getPhone();
                }
            } else if (user.getRole() == UserRole.ADMIN) {
                userName = "管理员";
            }

            return new LoginResponse(token, "Bearer", user.getId(), userName, phone, user.getRole().name(), user.getRelatedId());
        } else {
            // 用户不存在于 user 表，尝试从 patient 表查找（兼容旧数据）
            Patient patient = patientRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new BusinessException("用户名或密码错误"));

            // 如果患者没有设置密码，默认使用手机号后6位作为初始密码
            if (patient.getPassword() == null || patient.getPassword().isEmpty()) {
                String defaultPassword = request.getPhone().length() >= 6
                        ? request.getPhone().substring(request.getPhone().length() - 6)
                        : request.getPhone();
                if (!request.getPassword().equals(defaultPassword)) {
                    throw new BusinessException("用户名或密码错误");
                }
            } else {
                if (!passwordEncoder.matches(request.getPassword(), patient.getPassword())) {
                    throw new BusinessException("用户名或密码错误");
                }
            }

            // 为兼容旧数据，直接生成token（角色为PATIENT）
            String token = jwtUtil.generateToken(patient.getPhone(), patient.getId(), UserRole.PATIENT.name());
            return new LoginResponse(token, "Bearer", patient.getId(), patient.getName(), patient.getPhone(), UserRole.PATIENT.name(), patient.getId());
        }
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // 检查手机号是否已被注册
        if (userRepository.existsByUsername(request.getPhone())) {
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
        patient = patientRepository.save(patient);

        // 设置密码（如果未设置，使用手机号后6位）
        String password = request.getPassword();
        if (password == null || password.isEmpty()) {
            password = request.getPhone().length() >= 6
                    ? request.getPhone().substring(request.getPhone().length() - 6)
                    : request.getPhone();
        }

        // 创建用户实体（患者角色）
        User user = new User();
        user.setUsername(request.getPhone());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.PATIENT);
        user.setRelatedId(patient.getId());
        userRepository.save(user);

        // 生成token
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());

        return new LoginResponse(token, "Bearer", user.getId(), patient.getName(), patient.getPhone(), UserRole.PATIENT.name(), patient.getId());
    }
}