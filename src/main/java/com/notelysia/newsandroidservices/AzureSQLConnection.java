package com.notelysia.newsandroidservices;



import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

            String server = props.getProperty("server");
            String port = props.getProperty("port");
            String data = props.getProperty("database");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
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
