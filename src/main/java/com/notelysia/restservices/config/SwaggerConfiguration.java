/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.notelysia.restservices.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

@Configuration
public class SwaggerConfiguration {
    private static final Logger logger = LogManager.getLogger(SwaggerConfiguration.class);
    Properties props = new Properties();
    FileInputStream in;

    @Bean
    public OpenAPI customOpenAPI() {

        try {
            in = new FileInputStream("spring_conf/authkey.properties");
            props.load(in);
            in.close();
            return new OpenAPI()
                    //Edit footer, change /v3/api-docs
                    .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                            .description("Open Source Project NewsApp-RESTServices")
                            .url("https://github.com/2dgirlismywaifu/NewsApp-RESTServices"))
                    .info(new Info().title("NewsApp Reset API").version("2.0.0"))
                    //HTTP or HTTPS
                    // Components section defines Security Scheme "mySecretHeader"
                    .components(new Components()
                            .addSecuritySchemes("mySecretHeader", new SecurityScheme()
                                    .type(SecurityScheme.Type.APIKEY)
                                    .in(SecurityScheme.In.HEADER)
                                    .name(new String(Base64.getDecoder().decode(props.getProperty("auth-token-header-name"))))))

                    // AddSecurityItem section applies created scheme globally
                    .addSecurityItem(new SecurityRequirement().addList("mySecretHeader"));
        } catch (IOException e) {
            logger.error("Error : " + e, e);
            return null;
        }
    }
}
