package org.example.util.jdbc.dao;

import org.example.entity.MyEvent;
import org.example.util.jdbc.connector.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyEventDAOImpl implements MyEventDAO{


    @Override
    public void createTableMyEvent() throws Exception{ //метод по созданию таблицы
        Connection connection = ConnectionUtil.getMyH2Connection(); //получаем соединение
        String sql = "CREATE TABLE myEvent ( Id BIGSERIAL PRIMARY KEY, Name VARCHAR(64)," +
                " Date VARCHAR(64), City VARCHAR(64), Building VARCHAR(128))";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    } //ожидаем что таблица будет создана

    @Override
    public void saveMyEvent(MyEvent myEvent) throws Exception{ //метод по сохранению события в бд
        Connection connection = ConnectionUtil.getMyH2Connection(); //получаем соединение
        String sql = "INSERT INTO myEvent (Name, Date, City, Building) values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, myEvent.getName());
        preparedStatement.setString(2, myEvent.getDate());
        preparedStatement.setString(3, myEvent.getCity());
        preparedStatement.setString(4, myEvent.getBuilding());
        preparedStatement.executeUpdate();
    } // ожидаем что событие будет сохранено

    @Override
    public void updateMyEvent(MyEvent myEvent) throws Exception{ //метод по обновлению записи
        Connection connection = ConnectionUtil.getMyH2Connection(); //получаем соединение
        String sql = "UPDATE myEvent SET Name = ?, Date = ?, City = ?, Building = ? WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, myEvent.getName());
        preparedStatement.setString(2, myEvent.getDate());
        preparedStatement.setString(3, myEvent.getCity());
        preparedStatement.setString(4, myEvent.getBuilding());
        preparedStatement.setLong(5, myEvent.getId());
        preparedStatement.executeUpdate();
    } //ожидаем обновление записи в бд

    @Override
    public List<MyEvent> findAllMyEvent() throws Exception{ //метод по извлечению всех записей из таблицы myEvent
        List<MyEvent> list = new ArrayList<>();
        Connection connection = ConnectionUtil.getMyH2Connection(); //получаем соединение
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
        return list; //ожидаем получить список с событиями либо пустой список
    }

    @Override
    public MyEvent findByIdMyEvent(long id) throws Exception{ //метод получения события по id
        MyEvent myEvent = new MyEvent();
        Connection connection = ConnectionUtil.getMyH2Connection(); //получаем соединение
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
        return myEvent; //ожидаем получить событие либо пустое событие
    }

    @Override
    public MyEvent findByNameMyEvent(String name) throws Exception{ //метод получения события по названию
        MyEvent myEvent = new MyEvent();
        Connection connection = ConnectionUtil.getMyH2Connection(); //получаем соединение
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
        return myEvent; //ожидаем получить событие либо пустое событие
    }

    @Override
    public void deleteByIdMyEvent(long id) throws Exception{ //удалить запись по id
        Connection connection = ConnectionUtil.getMyH2Connection(); //получаем соединение
        String sql = "DELETE FROM myEvent WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    } //ожидаем что запись будет удалена (если нет такой записи то запрос просто проигнорируется)

    @Override
    public void deleteByIdListMyEvent(Set<MyEvent> set) throws Exception{ //удалить записи по id
        if(!set.isEmpty()) { //если лист не пустой тогда перебираем
            Connection connection = ConnectionUtil.getMyH2Connection(); //получаем соединение
            //преобразуем множество из событий в лист с id
            List<Long> list = set.stream().map(MyEvent::getId).collect(Collectors.toList());
            for (long id : list) { //в цикле удаляем; можно настроить "batch" что бы уаление происходило более "экономично"
                String sql = "DELETE FROM myEvent WHERE Id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        }
    } //ожидаем что все записи из множества будут удалены

}
