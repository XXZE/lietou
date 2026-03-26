package com.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:/files}")
    private String urlPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射,使上传的文件可以通过URL访问
        registry.addResourceHandler(urlPrefix + "/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
