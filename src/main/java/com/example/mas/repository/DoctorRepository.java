package com.example.mas.repository;

import com.example.mas.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 医生数据访问层
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByDepartmentId(Long departmentId);
    List<Doctor> findByAvailableTrue();
    Optional<Doctor> findByPhone(String phone);
    Optional<Doctor> findByEmail(String email);
}