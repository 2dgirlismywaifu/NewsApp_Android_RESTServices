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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class JPADatasourceConfig {
    //Create configuration for JPA use JpaProperties.class and DataSourceProperties.class
    //Use log4j2 for logging
    Logger logger = LogManager.getLogger(JPADatasourceConfig.class);
    //Create a bean for DataSource
    Properties props = new Properties();
    FileInputStream in;
    @Bean
    public DataSource dataSource() throws IOException {
        HikariConfig dataSourceConfig = new HikariConfig();
        in = new FileInputStream("spring_conf/db.properties");
        props.load(in);
        in.close();
        dataSourceConfig.setDriverClassName(props.getProperty("jdbc.driverClassName"));
        dataSourceConfig.setJdbcUrl(props.getProperty("jdbc.url"));
        dataSourceConfig.setUsername(props.getProperty("jdbc.username"));
        dataSourceConfig.setPassword(props.getProperty("jdbc.password"));
        return new HikariDataSource(dataSourceConfig);
    }
    //Add the other beans here
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
