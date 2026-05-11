package com.example.mas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医生创建请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCreateRequest {

    @NotBlank(message = "医生姓名不能为空")
    @Size(max = 50, message = "医生姓名长度不能超过50个字符")
    private String name;

    @Size(max = 50, message = "医生职称长度不能超过50个字符")
    private String title;

    @Size(max = 100, message = "专业领域长度不能超过100个字符")
    private String specialty;

    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @NotNull(message = "科室ID不能为空")
    private Long departmentId;
}