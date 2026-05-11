// D:\maven_project\mas\src\main\java\com\example\mas\dto\response\LoginResponse.java
package com.example.mas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String userName;
    private String phone;
}