package com.example.mas.service;

import com.example.mas.dto.request.LoginRequest;
import com.example.mas.dto.request.RegisterRequest;
import com.example.mas.dto.response.LoginResponse;

/**
 * 认证服务接口
 */
public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
}