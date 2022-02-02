package org.example.util.jdbc.dao;

import org.example.entity.MyEvent;
import org.example.util.jdbc.connector.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MyEventDAO {

    public void createTableMyEvent(){ //метод по созданию таблицы
        try(Connection connection = new ConnectionUtil().getMyH2Connection()){ //создаем соединение
            //Connection релизует интерфейс AutoCloseable
            //значит можно использовать try-with-resources
            String sql = "CREATE TABLE myEvent ( Id BIGSERIAL PRIMARY KEY, Name VARCHAR(64)," +
                    " Date VARCHAR(64), City VARCHAR(64), Building VARCHAR(128))";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    } //ожидаем что таблица будет создана

    public void saveMyEvent(MyEvent myEvent){ //метод по сохранению события в бд
        try(Connection connection = new ConnectionUtil().getMyH2Connection()) { //создаем соединение
            String sql = "INSERT INTO myEvent (Name, Date, City, Building) values (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, myEvent.getName());
            preparedStatement.setString(2, myEvent.getDate());
            preparedStatement.setString(3, myEvent.getCity());
            preparedStatement.setString(4, myEvent.getBuilding());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    } // ожидаем что событие будет сохранено

    public List<MyEvent> findAllMyEvent(){ //метод по извлечению всех записей из таблицы myEvent
        List<MyEvent> list = new ArrayList<>();
        try(Connection connection = new ConnectionUtil().getMyH2Connection()){ //создаем соединение
            String sql = "SELECT me.Id, me.Name, me.Date, me.City, me.Building FROM myEvent me";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return list; //ожидаем получить список с событиями либо пустой список
    }

    public MyEvent findByIdMyEvent(long id){ //метод получения события по id
        MyEvent myEvent = new MyEvent();
        try(Connection connection = new ConnectionUtil().getMyH2Connection()) { //создаем соединение
            String sql = "SELECT me.Id, me.Name, me.Date, me.City, me.Building FROM myEvent me WHERE me.Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                myEvent = new MyEvent().builder()
                        .id(resultSet.getLong("Id"))
                        .name(resultSet.getString("Name"))
                        .date(resultSet.getString("Date"))
                        .city(resultSet.getString("City"))
                        .building(resultSet.getString("Building"))
                        .build();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return myEvent; //ожидаем получить событие либо пустое событие
    }

    public MyEvent findByNameMyEvent(String name){ //метод получения события по названию
        MyEvent myEvent = new MyEvent();
        try(Connection connection = new ConnectionUtil().getMyH2Connection()) { //создаем соединение
            String sql = "SELECT me.Id, me.Name, me.Date, me.City, me.Building FROM myEvent me WHERE me.Name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                myEvent = new MyEvent().builder()
                        .id(resultSet.getLong("Id"))
                        .name(resultSet.getString("Name"))
                        .date(resultSet.getString("Date"))
                        .city(resultSet.getString("City"))
                        .building(resultSet.getString("Building"))
                        .build();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return myEvent; //ожидаем получить событие либо пустое событие
    }
}
