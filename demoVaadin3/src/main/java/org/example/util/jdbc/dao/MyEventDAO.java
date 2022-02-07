package org.example.util.jdbc.dao;

import org.example.entity.MyEvent;

import java.util.List;
import java.util.Set;

public interface MyEventDAO {
    void createTableMyEvent();
    void saveMyEvent(MyEvent myEvent);
    void updateMyEvent(MyEvent myEvent);
    List<MyEvent> findAllMyEvent();
    MyEvent findByIdMyEvent(long id);
    MyEvent findByNameMyEvent(String name);
    void deleteByIdMyEvent(long id);
    void deleteByIdListMyEvent(Set<MyEvent> set);
}
