package com.example.backend.config;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * PUBLIC_INTERFACE
 * Configures Cross-Origin Resource Sharing (CORS) for the API.
 *
 * Allowed origin is controlled by property: app.cors.allowed-origin
 */
@Configuration
@Tag(name = "Configuration")
public class WebCorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origin:http://localhost:3000}")
    private String allowedOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Apply to API endpoints only
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
