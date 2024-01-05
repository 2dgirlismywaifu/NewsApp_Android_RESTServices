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


import com.notelysia.restservices.model.entity.authkey.AuthApiKey;
import com.notelysia.restservices.service.authkey.AuthApiKeyServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
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
    @Autowired
    private AuthApiKeyServices authApiKeyServices;

    private List<AuthApiKey> getAuthToken(String headerName) {
        return this.authApiKeyServices.findByHeader(headerName);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            this.in = new FileInputStream("spring_conf/authkey.properties");
            this.props.load(this.in);
            this.in.close();
            String newsAppHeader = new String(Base64.getDecoder().decode(this.props.getProperty("auth-token-header-news-app")));
            String bookStoreHeader = new String(Base64.getDecoder().decode(this.props.getProperty("auth-token-header-news-app")));
            ApiKeyAuthFilter newsApp = new ApiKeyAuthFilter(newsAppHeader);
            ApiKeyAuthFilter bookStore = new ApiKeyAuthFilter(bookStoreHeader);

            newsApp.setAuthenticationManager(
                    authentication -> {
                        String principal = (String) authentication.getPrincipal();
                        for (AuthApiKey authApiKey : this.getAuthToken(newsAppHeader)) {
                            if (!Objects.equals(new String(Base64.getDecoder().decode(authApiKey.getToken())), principal)) {
                                throw new BadCredentialsException(
                                        "The api key does not have permission to access or not found!");
                            }
                            authentication.setAuthenticated(true);
                        }
                        return authentication;
                    });
            bookStore.setAuthenticationManager(
                    authentication -> {
                        String principal = (String) authentication.getPrincipal();
                        for (AuthApiKey authApiKey : this.getAuthToken(bookStoreHeader)) {
                            if (!Objects.equals(new String(Base64.getDecoder().decode(authApiKey.getToken())), principal)) {
                                throw new BadCredentialsException(
                                        "The api key does not have permission to access or not found!");
                            }
                            authentication.setAuthenticated(true);
                        }
                        return authentication;
                    });

            http.authorizeHttpRequests((request -> request
                            .requestMatchers(antMatcher("/news-api/**"), antMatcher("/news-app/**")).authenticated()))
                    .addFilter(newsApp)
                    .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(AbstractHttpConfigurer::disable);
            http.authorizeHttpRequests((request -> request
                            .requestMatchers(antMatcher("/book-store/**")).authenticated()))
                    .addFilter(bookStore)
                    .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(AbstractHttpConfigurer::disable);

            http.authorizeHttpRequests((request -> request.
                    //Exclude for Legacy Key Generator
                            requestMatchers(antMatcher("/legacy-key-generator/**")).permitAll().
                    //Exclude swagger from authentication
                            requestMatchers(antMatcher("/v3/api-docs/**"), antMatcher("/swagger-ui/**"), antMatcher("/swagger-ui.html")).permitAll()));
            return http.build();
        } catch (Exception e) {
            logger.error("Error: " + e, e);
            return null;
        }
    }

}


