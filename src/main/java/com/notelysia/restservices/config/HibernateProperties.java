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

import lombok.Getter;

import java.util.HashMap;
@Getter
public class HibernateProperties {
    private final String showSQL = "true";
    private final String formatSQL = "true";
    //This is Hibernate specific property for SQL Server
    public HashMap<String, Object> getSQLServerProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        properties.put("hibernate.default_schema", "dbo");
        return properties;
    }

    //This is Hibernate specific property for MySQL
    public HashMap<String, Object> getMySQLProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        return properties;
    }

    //This is Hibernate specific property for PostgreSQL
    public HashMap<String, Object> getPostgreSQLProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        return properties;
    }
    //This is Hibernate specific property for Oracle
    public HashMap<String, Object> getOracleProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        return properties;
    }
    //This is Hibernate specific property for H2
    public HashMap<String, Object> getH2Properties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        return properties;
    }
    //This is Hibernate specific property for HSQLDB
    public HashMap<String, Object> getHSQLDBProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        return properties;
    }
    //This is Hibernate specific property for MariaDB
    public HashMap<String, Object> getMariaDBProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        return properties;
    }
    //This is Hibernate specific property for DB2
    public HashMap<String, Object> getDB2Properties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.DB2Dialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        return properties;
    }
    //This is Hibernate specific property for Sybase
    public HashMap<String, Object> getSybaseProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.SybaseDialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        return properties;
    }
    //This is Hibernate specific property for MS Access
    public HashMap<String, Object> getMSAccessProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MsAccessDialect");
        properties.put("hibernate.show_sql", getShowSQL());
        properties.put("hibernate.format_sql", getFormatSQL());
        return properties;
    }
}
