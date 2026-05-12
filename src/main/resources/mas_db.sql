-- 医疗预约系统数据库初始化脚本
CREATE DATABASE IF NOT EXISTS mas_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mas_db;

-- 科室表
CREATE TABLE IF NOT EXISTS department (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '科室ID',
                                          name VARCHAR(100) NOT NULL UNIQUE COMMENT '科室名称',
                                          description VARCHAR(500) COMMENT '科室描述',
                                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室表';

-- 患者表
CREATE TABLE IF NOT EXISTS patient (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '患者ID',
                                       name VARCHAR(50) NOT NULL COMMENT '患者姓名',
                                       id_card VARCHAR(18) UNIQUE COMMENT '身份证号码',
                                       phone VARCHAR(20) NOT NULL COMMENT '联系电话',
                                       email VARCHAR(100) COMMENT '邮箱',
                                       password VARCHAR(255) COMMENT '密码',
                                       address VARCHAR(200) COMMENT '地址',
                                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者表';

-- 医生表
CREATE TABLE IF NOT EXISTS doctor (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '医生ID',
                                      name VARCHAR(50) NOT NULL COMMENT '医生姓名',
                                      title VARCHAR(50) COMMENT '职称',
                                      specialty VARCHAR(100) COMMENT '专业领域',
                                      phone VARCHAR(20) COMMENT '联系电话',
                                      email VARCHAR(100) COMMENT '邮箱',
                                      available TINYINT(1) DEFAULT 1 COMMENT '是否可用',
                                      department_id BIGINT COMMENT '所属科室ID',
                                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      CONSTRAINT fk_doctor_department FOREIGN KEY (department_id) REFERENCES department(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生表';

-- 预约状态枚举对应的预约表
CREATE TABLE IF NOT EXISTS appointment (
                                           id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '预约ID',
                                           patient_id BIGINT NOT NULL COMMENT '患者ID',
                                           doctor_id BIGINT NOT NULL COMMENT '医生ID',
                                           appointment_date DATETIME NOT NULL COMMENT '预约时间',
                                           status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '预约状态：PENDING-待确认，CONFIRMED-已确认，COMPLETED-已完成，CANCELLED-已取消',
                                           symptoms VARCHAR(500) COMMENT '症状描述',
                                           notes VARCHAR(500) COMMENT '备注',
                                           created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
                                           CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 插入初始科室数据
INSERT INTO department (name, description) VALUES
                                               ('内科', '负责内科疾病的诊断和治疗'),
                                               ('外科', '负责外科手术和相关疾病治疗'),
                                               ('妇产科', '负责妇科和产科相关疾病'),
                                               ('儿科', '负责儿童疾病的诊断和治疗'),
                                               ('急诊科', '负责急诊患者的紧急救治')
ON DUPLICATE KEY UPDATE description = VALUES(description);

-- 插入初始医生数据
INSERT INTO doctor (name, title, specialty, phone, department_id) VALUES
                                                                      ('张三', '主任医师', '心血管内科', '13800138001', 1),
                                                                      ('李四', '副主任医师', '消化内科', '13800138002', 1),
                                                                      ('王五', '主任医师', '骨科', '13800138003', 2),
                                                                      ('赵六', '主治医师', '普外科', '13800138004', 2),
                                                                      ('孙七', '主任医师', '妇产科', '13800138005', 3),
                                                                      ('周八', '副主任医师', '儿科', '13800138006', 4),
                                                                      ('吴九', '主治医师', '急诊科', '13800138007', 5)
ON DUPLICATE KEY UPDATE title = VALUES(title), specialty = VALUES(specialty), phone = VALUES(phone), department_id = VALUES(department_id);
-- 添加用户表
CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
                                    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名（手机号）',
                                    password VARCHAR(255) NOT NULL COMMENT '密码',
                                    role VARCHAR(20) NOT NULL COMMENT '角色：PATIENT, DOCTOR, ADMIN',
                                    related_id BIGINT COMMENT '关联的患者ID或医生ID',
                                    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 添加患者表的password字段
ALTER TABLE patient ADD COLUMN password VARCHAR(255) COMMENT '密码';

-- 插入默认管理员（密码：admin123）
INSERT INTO user (username, password, role) VALUES
    ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'ADMIN')
ON DUPLICATE KEY UPDATE password = VALUES(password);