package org.example.util.jdbc.dao;

import org.example.entity.MyAccount;
import org.example.util.jdbc.connector.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyAccountDAOImpl implements MyAccountDAO{

    @Override
    public void createTableMyAccount() throws Exception{ //метод по созданию таблицы myAccount
        Connection connection = ConnectionUtil.getMyH2Connection(); //создаем соединение
        String sql = "CREATE TABLE myAccount ( Id BIGSERIAL PRIMARY KEY, UserName VARCHAR(64) NOT NULL, Password VARCHAR(64) NOT NULL);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    } //ожидаем создание таблицы


    @Override
    public MyAccount findMyAccount(String userName, String password) throws Exception{ //метод по получению аккаунта по логину и паролю
        MyAccount myAccount = new MyAccount();
        Connection connection = ConnectionUtil.getMyH2Connection(); //создаем соединение
        String sql = "SELECT ma.Id, ma.UserName, ma.Password FROM myAccount ma " +
                "WHERE ma.UserName = ? and ma.Password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            myAccount = new MyAccount().builder()
                    .id(resultSet.getLong("Id"))
                    .userName(resultSet.getString("UserName"))
                    .password(resultSet.getString("Password"))
                    .build();
        }

        return myAccount; //ожидаем получить найденый аккунт либо пустой аккаунт
    }

    @Override
    public void saveMyAccount(String userName, String password) throws Exception{ //метод по созданию нового аккаунта
        Connection connection = ConnectionUtil.getMyH2Connection(); //создаем соединение
        String sql = "INSERT INTO myAccount(UserName, Password) values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();
    } //ожидаем сохраненный в бд новый аккаунт
}
