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

package com.notelysia.config.database;

import com.notelysia.config.HibernateProperties;
import jakarta.persistence.EntityManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "AuthKeyEntityManagerFactory",
    transactionManagerRef = "AuthKeyTransactionManager",
    basePackages = {"com.notelysia.restservices.repository.authkey"})
public class AuthKeyDataSourceConfig {
  private static final Logger logger = LogManager.getLogger(AuthKeyDataSourceConfig.class);
  // Create a bean for DataSource
  Properties props = new Properties();
  FileInputStream in;

  @Bean(name = "auth-key-datasource")
  public DataSource AuthKeySource() {
    DataSourceBuilder<?> dataSourceBuilder;
    try {
      this.in = new FileInputStream("spring_conf/db.properties");
      this.props.load(this.in);
      this.in.close();
      dataSourceBuilder = DataSourceBuilder.create();
      dataSourceBuilder.driverClassName(this.props.getProperty("jdbc.mariadb"));
      dataSourceBuilder.url(this.props.getProperty("jdbc.auth.url"));
      dataSourceBuilder.username(this.props.getProperty("jdbc.auth.username"));
      dataSourceBuilder.password(this.props.getProperty("jdbc.auth.password"));
      return dataSourceBuilder.build();
    } catch (IOException e) {
      logger.error("Error: " + e, e);
      return null;
    }
  }

  @Bean(name = "AuthKeyEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean thirdEntityManagerFactory(
      EntityManagerFactoryBuilder builder,
      @Qualifier("auth-key-datasource") DataSource AuthKeySource) {
    return builder
        .dataSource(AuthKeySource)
        .packages("com.notelysia.restservices.model.entity.authkey")
        .properties(new HibernateProperties().getMariaDBProperties())
        .build();
  }

  @Bean(name = "AuthKeyTransactionManager")
  public PlatformTransactionManager thirdTransactionManager(
      @Qualifier("AuthKeyEntityManagerFactory") EntityManagerFactory thirdEntityManagerFactory) {
    return new JpaTransactionManager(thirdEntityManagerFactory);
  }
}
