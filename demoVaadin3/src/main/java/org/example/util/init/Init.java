package org.example.util.init;


import org.example.entity.MyEvent;
import org.example.util.jdbc.dao.MyAccountDAO;
import org.example.util.jdbc.dao.MyEventDAO;

public class Init {

    private static Init instance;

    private Init(){}

    public static synchronized Init getInstance(){ //singleton, инициализацию возможно провести только один раз
        if(instance == null){
            instance = new Init();
            initial(); //проводим инициализацию данных sql
        }
        return instance; //возвращаем экземпляр, ожидаем созданные таблицы заполненые тестовыми данными
    }

    private static void initial(){ //создаем таблицы и добовляем тестовые записи
        try {
            System.out.println(">> Start init");
            MyAccountDAO myAccountDAO = new MyAccountDAO();
            MyEventDAO myEventDAO = new MyEventDAO();
            //создаем таблицы
            System.out.println(">> Create table");
            myAccountDAO.createTableMyAccount();
            myEventDAO.createTableMyEvent();
            //создаем тестовые данные
            //тестовый аккаунт
            System.out.println(">> Insert in table");
            myAccountDAO.saveMyAccount("admin","admin");
            //тестовые события
            myEventDAO.saveMyEvent(new MyEvent().builder().id(1L).name("Ded Dance").date("1984-01-01").city("Silent Hill").building("Church").build());
            myEventDAO.saveMyEvent(new MyEvent().builder().id(2L).name("The Divine Comedy").date("1111-01-01").city("Hell").building("Nine Circles of Hell").build());
            myEventDAO.saveMyEvent(new MyEvent().builder().id(3L).name("Sin City").date("2005-06-02").city("Sin City").building("...").build());
            System.out.println(">> Init to end");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
