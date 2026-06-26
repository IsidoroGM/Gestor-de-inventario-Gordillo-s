package com.invault.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Temporary security configuration for the API.
 * For now, only /api/health is public and the rest of the API remains protected.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Enables CORS support using the CorsConfig bean.
                .cors(Customizer.withDefaults())

                // CSRF is disabled because this backend will expose a stateless REST API.
                .csrf(AbstractHttpConfigurer::disable)

                // Defines which endpoints are public and which require authentication.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health").permitAll()
                        .anyRequest().authenticated()
                )

                // Temporary authentication mechanisms while JWT is not implemented.
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())

                .build();
    }
}


//Explication:
// This code defines a Spring Security configuration for a Java application. It is annotated with `@Configuration`, indicating that it is a configuration class. The `securityFilterChain` method is annotated with `@Bean`,
// which means it will be managed by the Spring container and can be injected into other components.
// The method takes an `HttpSecurity` object as a parameter and configures the security settings for the application.
// It disables CSRF protection, allows unauthenticated access to the `/api/health` endpoint, and requires authentication for all other requests.
// It also enables HTTP Basic authentication and form-based login with default settings.
// Finally, it builds and returns a `SecurityFilterChain` object that defines the security filter chain for the application.

// Este código define una configuración de Spring Security para una aplicación Java. Está anotado con `@Configuration
// `, lo que indica que se trata de una clase de configuración. El método `securityFilterChain` está anotado con `@Bean`,
// lo que significa que será gestionado por el contenedor Spring y podrá inyectarse en otros componentes.
// El método toma un objeto `HttpSecurity` como parámetro y configura los ajustes de seguridad de la aplicación.
// Desactiva la protección CSRF, permite el acceso sin autenticación al punto final `/api/health` y exige autenticación para todas las demás solicitudes.
// Además, habilita la autenticación HTTP básica y el inicio de sesión mediante formulario con la configuración predeterminada.
// Por último, crea y devuelve un objeto `SecurityFilterChain` que define la cadena de filtros de seguridad de la aplicación.