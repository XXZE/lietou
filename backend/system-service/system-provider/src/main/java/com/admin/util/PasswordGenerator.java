package com.admin.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后的密码: " + encodedPassword);
        System.out.println("长度: " + encodedPassword.length());

        // 验证
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("验证结果: " + matches);
    }
}
