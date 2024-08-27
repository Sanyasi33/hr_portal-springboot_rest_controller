package com.dr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain sfc(HttpSecurity http)throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorizeRequests-> authorizeRequests

                 // Public endpoints
                        .requestMatchers("/loginForToken").permitAll()
                .requestMatchers(HttpMethod.GET, "/tempLogin", "/resetPwd", "/denied", "/v3/api-docs", "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.POST, "/tempLogin", "/resetPwd").permitAll()

                 // HR endpoints
                .requestMatchers(HttpMethod.GET, "/getAllEmployees", "/getEmployee/{id}", "/viewAppliedLeaves", "/viewResignationRequests").hasAuthority("ROLE_HR")
                .requestMatchers(HttpMethod.POST, "/addEmployee").hasAuthority("ROLE_HR")
                .requestMatchers(HttpMethod.PUT, "/updateEmployee/{id}", "/approveLeave/{id}", "/rejectLeave/{id}", "/approveResign/{id}", "/rejectResign/{id}").hasAuthority("ROLE_HR")

                 // Employee endpoints
                .requestMatchers(HttpMethod.GET, "/viewSelfDetails/{id}", "/leaveStatus/{id}", "/resignStatus/{id}").hasAuthority("ROLE_EMPLOYEE")
                .requestMatchers(HttpMethod.POST, "/addCheckIn/{id}", "/applyLeave/{id}", "/resignRequest/{id}").hasAuthority("ROLE_EMPLOYEE")
                .requestMatchers(HttpMethod.PUT, "/addCheckOut/{id}").hasAuthority("ROLE_EMPLOYEE")

                 // All other requests need to be authenticated
                .anyRequest().authenticated()
        )
        .exceptionHandling(eh->eh
                .accessDeniedPage("/denied")
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(httpBasic->{});
        return http.build();
    }
}