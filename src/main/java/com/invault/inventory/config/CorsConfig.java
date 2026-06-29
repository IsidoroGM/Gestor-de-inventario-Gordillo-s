package com.invault.inventory.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Global CORS configuration for the backend.
 * This allows the Angular frontend to call the API while both apps run on different ports.
 */
@Configuration
public class CorsConfig {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Angular development server URL.
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // HTTP methods allowed from the frontend.
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // Headers allowed in requests from Angular.
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        // JWT will later be exposed through the Authorization header.
        configuration.setExposedHeaders(List.of("Authorization"));

        // We use JWT headers, not cookies, so credentials stay disabled for now.
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Applies this CORS configuration to every endpoint.
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

/*
 * This class defines which frontend origins can call the InVault backend.
 * In development, it allows Angular running on http://localhost:4200 to access the API.
 */