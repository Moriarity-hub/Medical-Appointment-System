package com.example.mas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 预约状态枚举
 */
public enum AppointmentStatus {
    PENDING,    // 待确认
    CONFIRMED,  // 已确认
    COMPLETED,  // 已完成
    CANCELLED   // 已取消
}