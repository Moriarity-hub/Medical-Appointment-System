package com.example.mas.service.impl;

import com.example.mas.dto.request.DoctorCreateRequest;
import com.example.mas.dto.response.DepartmentResponse;
import com.example.mas.dto.response.DoctorResponse;
import com.example.mas.entity.Department;
import com.example.mas.entity.Doctor;
import com.example.mas.exception.BusinessException;
import com.example.mas.repository.DepartmentRepository;
import com.example.mas.repository.DoctorRepository;
import com.example.mas.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医生服务实现类
 */
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public DoctorResponse createDoctor(DoctorCreateRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new BusinessException("科室不存在"));

        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setTitle(request.getTitle());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setDepartment(department);
        doctor = doctorRepository.save(doctor);
        return toResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("医生不存在"));
        return toResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsByDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new BusinessException("科室不存在");
        }
        return doctorRepository.findByDepartmentId(departmentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorCreateRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("医生不存在"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new BusinessException("科室不存在"));

        doctor.setName(request.getName());
        doctor.setTitle(request.getTitle());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setDepartment(department);
        doctor = doctorRepository.save(doctor);
        return toResponse(doctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new BusinessException("医生不存在");
        }
        doctorRepository.deleteById(id);
    }

    private DoctorResponse toResponse(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setName(doctor.getName());
        response.setTitle(doctor.getTitle());
        response.setSpecialty(doctor.getSpecialty());
        response.setPhone(doctor.getPhone());
        response.setEmail(doctor.getEmail());
        response.setAvailable(doctor.getAvailable());

        if (doctor.getDepartment() != null) {
            DepartmentResponse deptResponse = new DepartmentResponse();
            deptResponse.setId(doctor.getDepartment().getId());
            deptResponse.setName(doctor.getDepartment().getName());
            response.setDepartment(deptResponse);
        }

        response.setCreatedAt(doctor.getCreatedAt());
        response.setUpdatedAt(doctor.getUpdatedAt());
        return response;
    }
}