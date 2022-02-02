package org.example.util.jdbc.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    public Connection getMyH2Connection()throws SQLException, Exception { //метод по созданию соединения к бд
        String DRIVER = "org.h2.Driver";
        String URL = "jdbc:h2:mem:testdb";
        String USER_NAME = "sa";
        String PASSWORD = "";

        Class.forName(DRIVER);

        return DriverManager.getConnection(URL,USER_NAME,PASSWORD); //ожидаем состоявшийся экземпляр соединения
    }

    public void closeQuietly(Connection connection){ //метод по закрытию соединения
        try{
            connection.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void rollbackQuietly(Connection connection){ //откат изменений
        try {
            connection.rollback();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
