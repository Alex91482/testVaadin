package org.example.util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    public static Connection getMyH2Connection()throws SQLException, Exception {
        String DRIVER = "org.h2.Driver";
        String URL = "jdbc:h2:mem:testdb";
        String USER_NAME = "sa";
        String PASSWORD = "";

        Class.forName(DRIVER);

        return DriverManager.getConnection(URL,USER_NAME,PASSWORD);
    }

    public static void closeQuietly(Connection connection){
        try{
            connection.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void rollbackQuietly(Connection connection){
        try {
            connection.rollback();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
