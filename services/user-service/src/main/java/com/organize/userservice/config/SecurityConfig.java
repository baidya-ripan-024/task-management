package com.organize.userservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * This class contains all the security configurations for the application.
 * It configures the security filter chain which is used to validate the
 * JWT tokens present in the Authorization header of the request.
 */
@Slf4j
@Configuration
public class SecurityConfig {

    /**
     * This method is used to configure the security filter chain.
     * It sets up the stateless session policy, sets up the authorization
     * based on the roles, sets up the JWT token validation filter, disables
     * CSRF, sets up CORS and sets up HTTP basic authentication.
     *
     * @param http The HTTP security object to be configured.
     * @return The configured security filter chain.
     * @throws Exception In case of any configuration error.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        log.info("Configuring security filter chain.");

        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll())
                .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(Customizer.withDefaults());

        log.info("Security filter chain configured successfully");
        return http.build();
    }

    /**
     * This method is used to configure CORS.
     * It sets up the allowed origins, allowed methods, allowed headers,
     * exposed headers and max age.
     *
     * @return The configured CORS configuration source.
     */
    private CorsConfigurationSource corsConfigurationSource() {
        log.info("Setting up CORS Configuration.");

        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(Arrays.asList(
                        "http://localhost:3000", // React frontend Running on localhost
                        "https://example.com" // Deployed URL running in the cloud
                )); // From any frontend url, this backend would be accessible
                cfg.setAllowedMethods(Collections.singletonList("*")); // Allowing GET, POST, PUT, DELETE, OPTIONS
                cfg.setAllowCredentials(true);
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                cfg.setExposedHeaders(Arrays.asList("Authorization"));
                cfg.setMaxAge(3600L);

                log.debug("CORS configuration created: {}", cfg);
                return cfg;
            }
        };
    }

    /**
     * This method is used to create a password encoder bean.
     * It is used to hash the passwords.
     *
     * @return The password encoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Password encoder bean created.");
        return new BCryptPasswordEncoder();
    }
}