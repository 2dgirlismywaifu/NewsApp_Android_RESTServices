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

package com.notelysia.newsandroidservices.azure;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Logger;


public class AzureSQLConnection {

    public Connection getConnection()  {
        Connection con =null;
        Properties props = new Properties();
        InputStream in;
        ClassPathResource resource = new ClassPathResource("application.properties");
        try {
            in = resource.getInputStream();
            props.load(in);
            in.close();

            String server = new String(Base64.getDecoder().decode(props.getProperty("server")));
            String port = props.getProperty("port");
            String data = new String(Base64.getDecoder().decode(props.getProperty("database")));
            String username = new String(Base64.getDecoder().decode(props.getProperty("username")));
            String password = new String(Base64.getDecoder().decode(props.getProperty("password")));
            String dburl = "jdbc:sqlserver://" + server + ":" + port + ";databaseName=" + data
                    + ";username=" + username
                    + ";password=" + password
                    + ";encrypt=true;trustServerCertificate=false;"
                    + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            con = DriverManager.getConnection(dburl);
            return con;
        } catch (IOException | SQLException ex) {
            Logger.getLogger(AzureSQLConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return con;
    }
}
