package com.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan({"com.admin.system.mapper", "com.admin.crm.mapper"})
public class SystemProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemProviderApplication.class, args);
    }
}
