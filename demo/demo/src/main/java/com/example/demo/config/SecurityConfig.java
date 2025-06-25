package com.example.demo.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserAuthProvider userAuthProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(customizer -> customizer.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .sessionManagement(customizer ->
                        customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/cart/**").permitAll()
                .requestMatchers("/api/categories/**").permitAll()
                .requestMatchers("/api/orders/**").permitAll()
                .requestMatchers("/api/reviews/**").permitAll()
                .requestMatchers("/users/**").permitAll()
                .requestMatchers("/messages/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/products/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/favicon.ico/**").permitAll()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                );
        return http.build();
    }

}
