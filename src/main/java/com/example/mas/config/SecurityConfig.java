// D:\maven_project\mas\src\main\java\com\example\mas\config\SecurityConfig.java
package com.example.mas.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 * 用于配置Spring Boot应用的Security策略
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 允许访问登录和注册接口
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/logout").permitAll()
                        // 允许访问Swagger文档
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                        // 允许访问H2控制台
                        .requestMatchers("/h2-console/**").permitAll()
                        // 患者可访问的接口
                        .requestMatchers("/api/patients/me").hasAnyRole("PATIENT", "ADMIN")
                        .requestMatchers("/api/appointments/patient/**").hasAnyRole("PATIENT", "ADMIN")
                        .requestMatchers("/api/doctors").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        .requestMatchers("/api/doctors/department/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        .requestMatchers("/api/departments").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        .requestMatchers("/api/appointments").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        // 医生可访问的接口
                        .requestMatchers("/api/appointments/doctor/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/api/doctors/{id}").hasAnyRole("DOCTOR", "ADMIN")
                        // 管理员可访问的接口
                        .requestMatchers("/api/patients/**").hasRole("ADMIN")
                        .requestMatchers("/api/doctors/**").hasRole("ADMIN")
                        .requestMatchers("/api/departments/**").hasRole("ADMIN")
                        //医生权限
                        .requestMatchers("/api/appointments/doctor/**").hasRole("DOCTOR")
                        .requestMatchers("/api/doctors/me").hasRole("DOCTOR")
                        // 其他请求需要认证
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 允许H2控制台的frame显示
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}