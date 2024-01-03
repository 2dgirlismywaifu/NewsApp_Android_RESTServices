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
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "NewsAppEntityManagerFactory",
        transactionManagerRef = "NewsAppTransactionManager",
        basePackages = {"com.notelysia.restservices.newsapp.jparepo"})
public class NewsAppDatasourceConfig {
    private static final Logger logger = LogManager.getLogger(NewsAppDatasourceConfig.class);
    //Create a bean for DataSource
    Properties props = new Properties();
    FileInputStream in;

    @Primary
    @Bean(name = "newsapp_datasource")
    public DataSource newsappSource() {
        DataSourceBuilder<?> dataSourceBuilder;
        try {
            in = new FileInputStream("spring_conf/db.properties");
            props.load(in);
            in.close();
            dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName(props.getProperty("jdbc.sqlserver"));
            dataSourceBuilder.url(props.getProperty("jdbc.url"));
            dataSourceBuilder.username(props.getProperty("jdbc.username"));
            dataSourceBuilder.password(props.getProperty("jdbc.password"));
            return dataSourceBuilder.build();
        } catch (IOException e) {
            logger.error("Error: " + e, e);
            return null;
        }
    }

    @Primary
    @Bean(name = "NewsAppEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                              @Qualifier("newsapp_datasource")
                                                                              DataSource newsappSource) {
        return builder
                .dataSource(newsappSource)
                .packages("com.notelysia.restservices.newsapp.model")
                .properties(new HibernateProperties().getSQLServerProperties())
                .build();
    }

    @Bean(name = "NewsAppTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("NewsAppEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }


}
