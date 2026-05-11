package com.example.mas.repository;

import com.example.mas.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 患者数据访问层
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByIdCard(String idCard);
    Optional<Patient> findByPhone(String phone);
    Optional<Patient> findByEmail(String email);
    
    boolean existsByPhone(String phone);
    boolean existsByIdCard(String idCard);
}