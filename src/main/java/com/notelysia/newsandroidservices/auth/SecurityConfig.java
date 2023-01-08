package com.notelysia.newsandroidservices.auth;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Objects;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {

  @Value("${newsapp.http.auth-token-header-name}")
  private String principalRequestHeader;

  @Value("${newsapp.http.auth-token}")
  private String principalRequestValue;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    ApiKeyAuthFilter filter = new ApiKeyAuthFilter(new String(Base64.getDecoder().decode(principalRequestHeader)));
    filter.setAuthenticationManager(
        authentication -> {
          String principal = (String) authentication.getPrincipal();
          if (!Objects.equals(new String(Base64.getDecoder().decode(principalRequestValue)), principal)) {
            throw new BadCredentialsException(
                "The API key was not found or not the expected value.");
          }
          authentication.setAuthenticated(true);
          return authentication;
        });

    http.authorizeHttpRequests((request -> request.anyRequest().authenticated()))
        .addFilter(filter)
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilter(filter)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}