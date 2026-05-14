// D:\maven_project\mas\src\main\java\com\example\mas\config\DataInitializer.java
package com.example.mas.config;

import com.example.mas.entity.Doctor;
import com.example.mas.entity.User;
import com.example.mas.entity.UserRole;
import com.example.mas.repository.DoctorRepository;
import com.example.mas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器 - 启动时创建默认管理员用户和医生用户
 * 该类在应用启动时加载，确保数据库中存在默认的管理员用户和医生用户
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 查找或创建管理员用户
        User admin = userRepository.findByUsername("admin").orElse(new User());
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);
        System.out.println("默认管理员用户已初始化: admin / admin123");

        // 为所有医生创建用户记录（如果不存在）
        for (Doctor doctor : doctorRepository.findAll()) {
            if (doctor.getPhone() != null && !doctor.getPhone().isEmpty()) {
                User existingUser = userRepository.findByUsername(doctor.getPhone()).orElse(null);
                if (existingUser == null) {
                    // 创建新用户
                    User doctorUser = new User();
                    doctorUser.setUsername(doctor.getPhone());
                    // 使用手机号后6位作为初始密码
                    String defaultPassword = doctor.getPhone().length() >= 6
                            ? doctor.getPhone().substring(doctor.getPhone().length() - 6)
                            : doctor.getPhone();
                    doctorUser.setPassword(passwordEncoder.encode(defaultPassword));
                    doctorUser.setRole(UserRole.DOCTOR);
                    doctorUser.setRelatedId(doctor.getId());
                    userRepository.save(doctorUser);
                    System.out.println("医生用户已创建: " + doctor.getPhone() + " / " + defaultPassword);
                }
            }
        }
    }
}