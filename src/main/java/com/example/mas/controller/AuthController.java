// D:\maven_project\mas\src\main\java\com\example\mas\controller\AuthController.java
package com.example.mas.controller;

import com.example.mas.dto.request.LoginRequest;
import com.example.mas.dto.request.RegisterRequest;
import com.example.mas.dto.response.ApiResponse;
import com.example.mas.dto.response.LoginResponse;
import com.example.mas.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户登录和注册相关接口")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "患者使用手机号和密码登录系统")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "患者注册账号")
    public ResponseEntity<ApiResponse<LoginResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("注册成功", response));
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // 在实际应用中，可以在这里实现token黑名单逻辑
        return ResponseEntity.ok(ApiResponse.success("退出成功", null));
    }
}