package com.gymwalabackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private OAuthLoginSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/login/**", "/oauth2/**", "/error", "/").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> oauth.successHandler(successHandler))
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {}))
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedOrigins(List.of("http://localhost:8100"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/public/health").permitAll()
//                        .requestMatchers("/public/**").permitAll()
//                        .requestMatchers("/login/**").permitAll()
//                        .requestMatchers("/oauth2/**").permitAll()
//                        .requestMatchers("/error").permitAll()
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/api/**").authenticated()
//                        .anyRequest().permitAll()
//                ).oauth2Login(oauth -> oauth.successHandler(successHandler))
//                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {}))
//                .logout(logout -> logout
//                        .logoutUrl("/api/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                )
//
//                .oauth2Login(oauth -> oauth
//                        .successHandler(successHandler)
//                )
//                .oauth2ResourceServer(oauth -> oauth
//                        .jwt(jwt -> {})
//                );
//
//        return http.build();
//    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of(
//            "http://localhost:4200",
//            "http://localhost:3000",
//            "http://localhost:8080"
//        ));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChainLogout(HttpSecurity http) throws Exception {
//        http
//                .logout(logout -> logout
//                        .logoutUrl("/api/auth/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                );
//        return http.build();
//    }
}
