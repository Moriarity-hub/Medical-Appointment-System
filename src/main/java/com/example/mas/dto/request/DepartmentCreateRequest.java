package com.example.mas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 科室创建请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateRequest {

    @NotBlank(message = "科室名称不能为空")
    @Size(max = 100, message = "科室名称长度不能超过100个字符")
    private String name;

    @Size(max = 500, message = "科室描述长度不能超过500个字符")
    private String description;
}