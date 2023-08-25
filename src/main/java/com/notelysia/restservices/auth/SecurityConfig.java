/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notelysia.restservices.auth;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.io.FileInputStream;
import java.util.Base64;
import java.util.Objects;
import java.util.Properties;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {
    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);
    Properties props = new Properties();
    FileInputStream in;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            in = new FileInputStream("spring_conf/authkey.properties");
            props.load(in);
            in.close();
            ApiKeyAuthFilter filter = new ApiKeyAuthFilter(new String(Base64.getDecoder().decode(props.getProperty("auth-token-header-name"))));
            filter.setAuthenticationManager(
            authentication -> {
              String principal = (String) authentication.getPrincipal();
              if (!Objects.equals(new String(Base64.getDecoder().decode(props.getProperty("auth-token"))), principal)) {
                throw new BadCredentialsException(
                    "The API key was not found or not the expected value.");
              }
              authentication.setAuthenticated(true);
              return authentication;
            });
            http.authorizeHttpRequests((request -> request
                            //Exclude swagger from authentication
                            .requestMatchers(antMatcher("/v3/api-docs/**"), antMatcher("/swagger-ui/**"), antMatcher("/swagger-ui.html")).permitAll()
                            //Exclude for Legacy Key Generator
                            .requestMatchers(antMatcher("/api/v2/win"), antMatcher("/api/v2/office")).permitAll()
                            .anyRequest().authenticated()))
                .addFilter(filter)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilter(filter)
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(
                    sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            return http.build();
        } catch (Exception e) {
            logger.error("Error: " + e, e);
            return null;
        }
    }

}


