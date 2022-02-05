package org.example.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import org.example.MyUI;
import org.example.entity.MyEvent;
import org.example.util.jdbc.dao.MyEventDAOImpl;

import java.util.List;

public class GridView extends VerticalLayout implements View {

    private  Grid<MyEvent> grid1;
    private boolean windowsOpen = false; //флаг открыто ли окно для изменения/добавления MyEvent


    private MyEvent myEvent; //экземпляр в который записывается/перезаписывается выбранное по клику событие


    public GridView(){
        myEvent = new MyEvent(); //экземпляр в который записывается/перезаписывается выбранное по клику событие
        HorizontalLayout horizontalLayout = new HorizontalLayout(); //часть с кнопками
        grid1 = makeATable(); //таблица
        insertAllMyEvent(); //заполнение данными
        grid1.setSizeFull(); //таблица на весь экран

        grid1.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid1.addItemClickListener(event -> {
            //builder почемуто не пишет так что сохраняем через сет
            myEvent.setId(event.getItem().getId());
            myEvent.setName(event.getItem().getName());
            myEvent.setDate(event.getItem().getDate());
            myEvent.setCity(event.getItem().getCity());
            myEvent.setBuilding(event.getItem().getBuilding());
        });

        Button buttonCreate = new Button("Create");
        buttonCreate.addClickListener(event -> {
            //создание события
            //вызываем функцию проверки состояния окна и передаем в нее названия кнопки вызова
            checkExists("create");
        });
        Button buttonDelete = new Button("Delete");
        buttonDelete.addClickListener(event -> {
            //вызов функции по удалению событий из бд
            deleteMyEvent(myEvent);
            insertAllMyEvent(); //запрашиваем из бд данные что бы обновить таблицу
        });
        Button buttonEdit = new Button("Edit");
        buttonEdit.addClickListener(event -> {
            //обновление события
            //вызываем функцию проверки состояния окна и передаем в нее названия кнопки вызова
            checkExists("edit");
        });
        horizontalLayout.addComponents(buttonCreate, buttonDelete, buttonEdit);

        this.addComponents(grid1, horizontalLayout);
    }


    private void checkExists(String value){ //метод по проверки того существует ли окно
        if (!windowsOpen) { //если флаг false то создаем окно
            System.out.println(">> Editor Event open");

            Window subWindow = new WindowView().makeAWindow(value,myEvent);

            MyUI.getCurrent().addWindow(subWindow); //добавляем окно на страницу
            windowsOpen = true; //флаг окно созданно
            subWindow.addCloseListener(e -> {
                insertAllMyEvent(); //обновляем таблицу после закрытия окна
                windowsOpen = false; //флаг окно закрыто
                System.out.println(">> Editor Event close");
            });
        }
    }

    private Grid<MyEvent> makeATable(){ //метод по созданию таблицы
        Grid<MyEvent> grid = new Grid<>();
        grid.addColumn(MyEvent::getId).setCaption("Id");
        grid.addColumn(MyEvent::getName).setCaption("Name");
        grid.addColumn(MyEvent::getDate).setCaption("Date");
        grid.addColumn(MyEvent::getCity).setCaption("City");
        grid.addColumn(MyEvent::getBuilding).setCaption("Building");

        return grid; //ожидаем озданную таблицу на странице
    }

    private void insertAllMyEvent(){ //метод по заполнению таблицы данными из бд
        System.out.println(">> Fill the table with events");
        List<MyEvent> list = new MyEventDAOImpl().findAllMyEvent(); //получить все события
        grid1.setItems(list); //все события из бд записываем в таблицу
    }

    private void deleteMyEvent(MyEvent myEvent){ //метод по удалению выбранного события
        System.out.println(">> Deleting events from id " + myEvent.getId());
        new MyEventDAOImpl().deleteByIdMyEvent(myEvent.getId());
        //что бы исключить возможность корректировки удаленного события назначаем глобальному myEvent новый экземпляр
        //например удаляем событие и сразу же нажимаем кнопку редактировать
        //валидация на событие пройдет потому что событие по прежнему будет сохранено в переменной myEvent
        //id != 0 и name не null поэтому присваеваем новый экземпляр
        this.myEvent = new MyEvent();
    }

}
