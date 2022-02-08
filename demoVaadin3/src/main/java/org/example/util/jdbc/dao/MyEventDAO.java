package org.example.util.jdbc.dao;

import org.example.entity.MyEvent;

import java.util.List;
import java.util.Set;

public interface MyEventDAO {
    void createTableMyEvent() throws Exception;
    void saveMyEvent(MyEvent myEvent) throws Exception;
    void updateMyEvent(MyEvent myEvent) throws Exception;
    List<MyEvent> findAllMyEvent() throws Exception;
    MyEvent findByIdMyEvent(long id) throws Exception;
    MyEvent findByNameMyEvent(String name) throws Exception;
    void deleteByIdMyEvent(long id) throws Exception;
    void deleteByIdListMyEvent(Set<MyEvent> set) throws Exception;
}
