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

package com.notelysia.newsandroidservices;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableJpaRepositories({"com.notelysia.newsandroidservices.jparepo"})
@EntityScan({"com.notelysia.newsandroidservices.model"})
@EnableWebMvc
public class NewsandroidRestApplication {

    @Bean
    public WebMvcConfigurer customConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureContentNegotiation(@NotNull ContentNegotiationConfigurer configurer) {
                configurer.defaultContentType(MediaType.APPLICATION_JSON);
            }
        };
    }
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(NewsandroidRestApplication.class);
        //replace 2984 with your desired port number
        app.setDefaultProperties(java.util.Collections.singletonMap("server.port", "2984"));
        app.run(args);
    }

}
