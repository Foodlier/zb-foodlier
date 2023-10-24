package com.zerobase.foodlier.common.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedOrigins("https://zb-foodlier.vercel.app")
                .allowedOrigins("http://localhost:5173")
                .allowedOrigins("http://localhost:8080")
                .allowedOrigins("http://ec2-15-165-55-217.ap-northeast-2.compute.amazonaws.com")
                .allowedOrigins("http://ec2-13-209-238-113.ap-northeast-2.compute.amazonaws.com")
                .allowedMethods("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }
}
