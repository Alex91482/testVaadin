package org.example.util.init;


import org.example.entity.MyEvent;
import org.example.util.jdbc.dao.MyAccountDAO;
import org.example.util.jdbc.dao.MyAccountDAOImpl;
import org.example.util.jdbc.dao.MyEventDAO;
import org.example.util.jdbc.dao.MyEventDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Init {

    private static final Logger logger = LoggerFactory.getLogger(Init.class);
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
            logger.info(">> Start init");
            MyAccountDAO myAccountDAOImpl = new MyAccountDAOImpl();
            MyEventDAO myEventDAOImpl = new MyEventDAOImpl();
            //создаем таблицы
            logger.info(">> Create table");
            myAccountDAOImpl.createTableMyAccount();
            myEventDAOImpl.createTableMyEvent();
            //создаем тестовые данные
            //тестовый аккаунт
            logger.info(">> Insert in table");
            myAccountDAOImpl.saveMyAccount("admin","admin");
            //тестовые события
            myEventDAOImpl.saveMyEvent(new MyEvent().builder().id(1L).name("Ded Dance").date("1984-01-01").city("Silent Hill").building("Church").build());
            myEventDAOImpl.saveMyEvent(new MyEvent().builder().id(2L).name("The Divine Comedy").date("1111-01-01").city("Hell").building("Nine Circles of Hell").build());
            myEventDAOImpl.saveMyEvent(new MyEvent().builder().id(3L).name("Sin City").date("2005-06-02").city("Sin City").building("unknown").build());
            myEventDAOImpl.saveMyEvent(new MyEvent().builder().id(4L).name("Dunwich horror").date("1929-06-06").city("Dunwich").building("Library ").build());
            myEventDAOImpl.saveMyEvent(new MyEvent().builder().id(5L).name("Call of Cthulhu").date("1981-06-06").city("England").building("Пх’нглуи мглв’нафх Ктулху Р’льех вгах’нагл фхтагн").build());
            myEventDAOImpl.saveMyEvent(new MyEvent().builder().id(6L).name("Azathoth").date("1938-06-06").city("Universe").building("Center of the galaxy").build());
            myEventDAOImpl.saveMyEvent(new MyEvent().builder().id(7L).name("Dagon").date("1912-06-06").city("Pacific Ocean").building("unknown").build());
            logger.info(">> Init to end");
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
