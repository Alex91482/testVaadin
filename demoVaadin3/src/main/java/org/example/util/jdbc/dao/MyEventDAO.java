package org.example.util.jdbc.dao;

import org.example.entity.MyEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyEventDAO {

    //DataSource dataSource

    public static List<MyEvent> findAllMyEvent(Connection connection) throws SQLException{
        String sql = "SELECT me.Id, me.Name, me.Date, me.City, me.Building FROM myEvent me";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<MyEvent> list = new ArrayList<>();
        while(resultSet.next()){
            list.add(
                    new MyEvent().builder()
                            .id(resultSet.getLong("Id"))
                            .name(resultSet.getString("Name"))
                            .date(resultSet.getString("Date"))
                            .city(resultSet.getString("City"))
                            .building(resultSet.getString("Building"))
                            .build()
            );
        }
        return list;
    }

    public static MyEvent findByIdMyEvent(Connection connection, long id)throws SQLException {
        String sql = "SELECT me.Id, me.Name, me.Date, me.City, me.Building FROM myEvent me WHERE me.Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            MyEvent myEvent = new MyEvent().builder()
                    .id(resultSet.getLong("Id"))
                    .name(resultSet.getString("Name"))
                    .date(resultSet.getString("Date"))
                    .city(resultSet.getString("City"))
                    .building(resultSet.getString("Building"))
                    .build();
            return myEvent;
        }
        return null;
    }
}
