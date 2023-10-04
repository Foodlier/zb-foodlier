package com.zerobase.foodlier.common.security.config;

import com.zerobase.foodlier.common.security.filter.JwtAuthenticationFilter;
import com.zerobase.foodlier.common.security.filter.JwtExceptionFilter;
import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.jwtExceptionFilter = new JwtExceptionFilter();
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity
                .addFilterBefore(
                        this.jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(this.jwtExceptionFilter, JwtAuthenticationFilter.class)
                .authorizeHttpRequests()
                .anyRequest().authenticated();
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers(
                        "/",
                        "/images/**",
                        "/js/**",
                        "/css/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v2/**",
                        "/v3/**",
                        "/**/auth/signup",
                        "/**/auth/login",
                        "/**/authKey/send",
                        "/**/auth/verify",
                        "/**/auth/signin",
                        "/**/success/**",
                        "/**/fail/**",
                        "/ws/**"
                );
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
