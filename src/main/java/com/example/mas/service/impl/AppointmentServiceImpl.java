package com.example.mas.service.impl;

import com.example.mas.dto.request.AppointmentCreateRequest;
import com.example.mas.dto.request.AppointmentStatusRequest;
import com.example.mas.dto.response.AppointmentResponse;
import com.example.mas.dto.response.DoctorResponse;
import com.example.mas.dto.response.PatientResponse;
import com.example.mas.entity.Appointment;
import com.example.mas.entity.AppointmentStatus;
import com.example.mas.entity.Doctor;
import com.example.mas.entity.Patient;
import com.example.mas.exception.BusinessException;
import com.example.mas.repository.AppointmentRepository;
import com.example.mas.repository.DoctorRepository;
import com.example.mas.repository.PatientRepository;
import com.example.mas.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预约服务实现类
 */
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(AppointmentCreateRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new BusinessException("患者不存在"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new BusinessException("医生不存在"));

        // 检查医生是否可用
        if (!Boolean.TRUE.equals(doctor.getAvailable())) {
            throw new BusinessException("医生当前不可预约");
        }

        // 检查该医生在同一时间是否已有预约
        if (appointmentRepository.existsByDoctorIdAndAppointmentDate(
                doctor.getId(), request.getAppointmentDate())) {
            throw new BusinessException("该医生在该时间段已有预约");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setSymptoms(request.getSymptoms());
        appointment.setNotes(request.getNotes());
        appointment = appointmentRepository.save(appointment);
        return toResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("预约不存在"));
        return toResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new BusinessException("患者不存在");
        }
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new BusinessException("医生不存在");
        }
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateBetween(start, end).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponse updateAppointmentStatus(Long id, AppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("预约不存在"));

        try {
            AppointmentStatus status = AppointmentStatus.valueOf(request.getStatus().toUpperCase());
            appointment.setStatus(status);
            appointment = appointmentRepository.save(appointment);
            return toResponse(appointment);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("无效的预约状态");
        }
    }

    @Override
    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("预约不存在"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setAppointmentDate(appointment.getAppointmentDate());
        response.setStatus(appointment.getStatus().name());
        response.setSymptoms(appointment.getSymptoms());
        response.setNotes(appointment.getNotes());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setUpdatedAt(appointment.getUpdatedAt());

        // 设置患者信息
        Patient patient = appointment.getPatient();
        PatientResponse patientResponse = new PatientResponse();
        patientResponse.setId(patient.getId());
        patientResponse.setName(patient.getName());
        patientResponse.setPhone(patient.getPhone());
        response.setPatient(patientResponse);

        // 设置医生信息
        Doctor doctor = appointment.getDoctor();
        DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.setId(doctor.getId());
        doctorResponse.setName(doctor.getName());
        doctorResponse.setTitle(doctor.getTitle());
        doctorResponse.setSpecialty(doctor.getSpecialty());
        response.setDoctor(doctorResponse);

        return response;
    }
}