package org.example.util.jdbc.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {

    private ConnectionUtil(){}

    private final Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);

    private static Connection connection; //экземпляр который будем возвращать

    public static Connection getMyH2Connection()throws Exception { //метод по созданию соединения к бд
        if(connection == null){
            String DRIVER = "org.h2.Driver";
            String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // ;DB_CLOSE_DELAY=-1 означает что после закрытия соединения не удалять данные
            String USER_NAME = "sa";
            String PASSWORD = "";

            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL,USER_NAME,PASSWORD);
        }
        return connection; //ожидаем состоявшийся экземпляр соединения
    }

    public void closeQuietly(Connection connection){ //метод по закрытию соединения
        try{
            connection.close();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public void rollbackQuietly(Connection connection){ //откат изменений
        try {
            connection.rollback();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
