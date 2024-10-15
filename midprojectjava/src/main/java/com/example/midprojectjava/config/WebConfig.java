package com.example.midprojectjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // API 경로
                .allowedOrigins("http://localhost:3000") // Next.js URL
                .allowedMethods("GET", "POST", "PUT", "DELETE" , "OPTIONS") // 허용할 HTTP 메소드
                .allowedHeaders("*")
        		.allowCredentials(true); // 쿠키, 인증 정보 허용
    }
}
