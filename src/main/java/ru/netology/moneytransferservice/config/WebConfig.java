package ru.netology.moneytransferservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Разрешаем все пути
                .allowedOrigins("https://serp-ya.github.io", "http://localhost:5500") // Разрешаем эти домены
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Разрешаем эти методы
                .allowedHeaders("*") // Разрешаем любые заголовки
                .allowCredentials(false); // Если не используем куки/авторизацию, ставим false
    }
}