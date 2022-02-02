package org.example.util.init;


import org.example.entity.MyEvent;
import org.example.util.jdbc.dao.MyAccountDAO;
import org.example.util.jdbc.dao.MyEventDAO;

public class Init {

    //спроэктировать так что бы было возможно запустить лишь раз
    //возможно синглтон

    public void initial(){
        try {
            MyAccountDAO myAccountDAO = new MyAccountDAO();
            MyEventDAO myEventDAO = new MyEventDAO();
            //создаем таблицы
            myAccountDAO.createTableMyAccount();
            myEventDAO.createTableMyEvent();
            //создаем тестовые данные
            //тестовый аккаунт
            myAccountDAO.saveMyAccount("admin","admin");
            //тестовые события
            myEventDAO.saveMyEvent(new MyEvent().builder().id(1L).name("Ded Dance").date("1984-01-01").city("Silent Hill").building("Church").build());
            myEventDAO.saveMyEvent(new MyEvent().builder().id(2L).name("The Divine Comedy").date("1111-01-01").city("Hell").building("Nine Circles of Hell").build());
            myEventDAO.saveMyEvent(new MyEvent().builder().id(3L).name("Sin City").date("2005-06-02").city("Sin City").building("...").build());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
