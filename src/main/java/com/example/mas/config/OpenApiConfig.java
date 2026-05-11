package com.example.mas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI配置类
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("医疗预约系统 API")
                        .version("1.0.0")
                        .description("医疗预约系统（Medical Appointment System）提供患者预约、医生管理、科室管理等功能")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com")));
    }
}