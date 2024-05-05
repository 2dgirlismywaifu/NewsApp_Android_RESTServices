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
    entityManagerFactoryRef = "AgencyStoreEntityManagerFactory",
    transactionManagerRef = "AgencyStoreTransactionManager",
    basePackages = {"com.notelysia.restservices.repository.agencystore"})
public class AgencyStoreDataSourceConfig {
  private static final Logger logger = LogManager.getLogger(AgencyStoreDataSourceConfig.class);
  // Create a bean for DataSource
  Properties props = new Properties();
  FileInputStream in;

  @Bean(name = "agency-store-datasource")
  public DataSource agencyStoreSource() {
    DataSourceBuilder<?> dataSourceBuilder;
    try {
      this.in = new FileInputStream("spring_conf/db.properties");
      this.props.load(this.in);
      this.in.close();
      dataSourceBuilder = DataSourceBuilder.create();
      dataSourceBuilder.driverClassName(this.props.getProperty("jdbc.mariadb"));
      dataSourceBuilder.url(this.props.getProperty("jdbc.third.url"));
      dataSourceBuilder.username(this.props.getProperty("jdbc.third.username"));
      dataSourceBuilder.password(this.props.getProperty("jdbc.third.password"));
      return dataSourceBuilder.build();
    } catch (IOException e) {
      logger.error("Error: " + e, e);
      return null;
    }
  }

  @Bean(name = "AgencyStoreEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean thirdEntityManagerFactory(
      EntityManagerFactoryBuilder builder,
      @Qualifier("agency-store-datasource") DataSource agencyStoreSource) {
    return builder
        .dataSource(agencyStoreSource)
        .packages("com.notelysia.restservices.model.entity.agencystore")
        .properties(new HibernateProperties().getMariaDBProperties())
        .build();
  }

  @Bean(name = "AgencyStoreTransactionManager")
  public PlatformTransactionManager thirdTransactionManager(
      @Qualifier("AgencyStoreEntityManagerFactory")
          EntityManagerFactory thirdEntityManagerFactory) {
    return new JpaTransactionManager(thirdEntityManagerFactory);
  }
}
