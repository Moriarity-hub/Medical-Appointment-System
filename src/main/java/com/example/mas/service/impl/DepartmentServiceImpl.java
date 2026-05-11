package com.example.mas.service.impl;

import com.example.mas.dto.request.DepartmentCreateRequest;
import com.example.mas.dto.response.DepartmentResponse;
import com.example.mas.entity.Department;
import com.example.mas.exception.BusinessException;
import com.example.mas.repository.DepartmentRepository;
import com.example.mas.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 科室服务实现类
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new BusinessException("科室名称已存在");
        }
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department = departmentRepository.save(department);
        return toResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("科室不存在"));
        return toResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentCreateRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("科室不存在"));

        if (!department.getName().equals(request.getName()) &&
                departmentRepository.existsByName(request.getName())) {
            throw new BusinessException("科室名称已存在");
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department = departmentRepository.save(department);
        return toResponse(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new BusinessException("科室不存在");
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentResponse toResponse(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setName(department.getName());
        response.setDescription(department.getDescription());
        response.setCreatedAt(department.getCreatedAt());
        response.setUpdatedAt(department.getUpdatedAt());
        return response;
    }
}