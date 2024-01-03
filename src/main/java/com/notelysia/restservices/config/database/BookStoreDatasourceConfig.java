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

package com.notelysia.restservices.config.database;

import com.notelysia.restservices.config.HibernateProperties;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "BookStoreEntityManagerFactory",
        transactionManagerRef = "BookStoreTransactionManager",
        basePackages = {"com.notelysia.restservices.bookstore.jparepo"})
public class BookStoreDatasourceConfig {
    private static final Logger logger = LogManager.getLogger(BookStoreDatasourceConfig.class);
    //Create a bean for DataSource
    Properties props = new Properties();
    FileInputStream in;

    @Bean(name = "bookstore_datasource")
    public DataSource bookStoreSource() {
        DataSourceBuilder<?> dataSourceBuilder;
        try {
            in = new FileInputStream("spring_conf/db.properties");
            props.load(in);
            in.close();
            dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName(props.getProperty("jdbc.sqlserver"));
            dataSourceBuilder.url(props.getProperty("jdbc.second.url"));
            dataSourceBuilder.username(props.getProperty("jdbc.second.username"));
            dataSourceBuilder.password(props.getProperty("jdbc.second.password"));
            return dataSourceBuilder.build();
        } catch (IOException e) {
            logger.error("Error: " + e, e);
            return null;
        }
    }


    @Bean(name = "BookStoreEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                             @Qualifier("bookstore_datasource")
                                                                             DataSource bookstoreSource) {
        return builder
                .dataSource(bookstoreSource)
                .packages("com.notelysia.restservices.bookstore.model")
                .properties(new HibernateProperties().getSQLServerProperties())
                .build();
    }

    @Bean(name = "BookStoreTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("BookStoreEntityManagerFactory") EntityManagerFactory secondEntityManagerFactory) {
        return new JpaTransactionManager(secondEntityManagerFactory);
    }


}
