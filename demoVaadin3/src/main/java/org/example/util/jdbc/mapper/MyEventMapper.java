package org.example.util.jdbc.mapper;

import org.example.entity.MyEvent;


import javax.swing.tree.RowMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyEventMapper /*implements RowMapper<MyEvent>*/ {

    public static final String BSE_SQL_MY_EVENT="SELECT me.Id, me.Name, me.Date, me.City, me.Building FROM myEvent me";


    /*@Override
    public MyEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("Id");
        String name = rs.getString("Name");
        String date = rs.getString("Date");
        String city = rs.getString("City");
        String building = rs.getString("Building");

        return new MyEvent(id, name,date,city, building);
    }*/
}
