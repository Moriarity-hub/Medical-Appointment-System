package com.example.mas.service;

import com.example.mas.dto.request.AppointmentCreateRequest;
import com.example.mas.dto.request.AppointmentStatusRequest;
import com.example.mas.dto.response.AppointmentResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约服务接口
 */
public interface AppointmentService {
    AppointmentResponse createAppointment(AppointmentCreateRequest request);
    AppointmentResponse getAppointmentById(Long id);
    List<AppointmentResponse> getAllAppointments();
    List<AppointmentResponse> getAppointmentsByPatient(Long patientId);
    List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId);
    List<AppointmentResponse> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end);
    AppointmentResponse updateAppointmentStatus(Long id, AppointmentStatusRequest request);
    void cancelAppointment(Long id);
}