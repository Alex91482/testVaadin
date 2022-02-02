package org.example.util.jdbc.dao;

import org.example.entity.MyAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyAccountDAO {

    public void createTableMyAccount(Connection connection) throws SQLException{
        String sql = "CREATE TABLE myAccount ( Id BIGSERIAL PRIMARY KEY, UserName VARCHAR(64), Password VARCHAR(64));";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }

    public MyAccount findMyAccount(Connection connection, String userName, String password)throws SQLException {
        String sql = "SELECT ma.Id, ma.UserName, ma.Password FROM myAccount ma " +
                "WHERE ma.UserName = ? and ma.Password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return new MyAccount().builder()
                    .id(resultSet.getLong("Id"))
                    .userName(resultSet.getString("UserName"))
                    .password(resultSet.getString("Password"))
                    .build();
        }

        return null;
    }

    public void saveAccount(Connection connection, String userName, String password)throws SQLException{
        String sql = "INSERT INTO myAccount(UserName, Password) values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();
    }
}
