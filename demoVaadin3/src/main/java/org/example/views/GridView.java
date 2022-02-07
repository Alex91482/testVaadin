package org.example.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.example.MyUI;
import org.example.entity.MyEvent;
import org.example.util.jdbc.dao.MyEventDAOImpl;

import java.util.List;

public class GridView extends VerticalLayout implements View {

    private Grid<MyEvent> grid1;
    private boolean windowsOpen = false; //флаг открыто ли окно для изменения/добавления MyEvent

    private Button buttonCreate;
    private Button buttonDelete;
    private Button buttonEdit;

    public GridView() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(); //часть с кнопками
        grid1 = makeATable(); //таблица
        insertAllMyEvent(); //заполнение данными
        grid1.setSizeFull(); //таблица на весь экран

        grid1.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid1.addItemClickListener(event -> { //слушатель на двойной клик
            if (event.getMouseEventDetails().isDoubleClick()) {
                checkExists(event.getItem());
            }
        });
        grid1.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) { //если значение выбранно то можно редактировать и удалять
                buttonDelete.setEnabled(true);
                buttonEdit.setEnabled(true);
            } else {
                buttonDelete.setEnabled(false);
                buttonEdit.setEnabled(false);
            }
        });

        buttonCreate = new Button("Create");
        buttonCreate.addStyleName(ValoTheme.BUTTON_FRIENDLY); //изменение цвета кнопки на зеленый
        buttonCreate.addClickListener(event -> {
            //создание события
            //вызываем функцию проверки состояния окна и передаем null
            checkExists(null);
        });
        buttonDelete = new Button("Delete");
        buttonDelete.addStyleName(ValoTheme.BUTTON_DANGER); //изменение цвета кнопки на красный
        buttonDelete.setEnabled(false);
        buttonDelete.addClickListener(event -> {
            //вызов функции по удалению событий из бд
            deleteMyEvent(grid1.getSelectedItems().stream().findFirst().get());
            insertAllMyEvent(); //запрашиваем из бд данные что бы обновить таблицу
        });
        buttonEdit = new Button("Edit");
        buttonEdit.addStyleName("myButton");
        buttonEdit.setEnabled(false);
        buttonEdit.addClickListener(event -> {
            //обновление события
            //вызываем функцию проверки состояния окна и передаем в нее названия кнопки вызова
            checkExists(grid1.getSelectedItems().stream().findFirst().get());
        });
        horizontalLayout.addComponents(buttonCreate, buttonDelete, buttonEdit);

        this.addComponents(grid1, horizontalLayout);
    }


    private void checkExists(MyEvent myEvent) { //метод по проверки того существует ли окно
        if (!windowsOpen) { //если флаг false то создаем окно
            System.out.println(">> Editor Event open");

            Window subWindow = new WindowView().window(myEvent);

            MyUI.getCurrent().addWindow(subWindow); //добавляем окно на страницу
            windowsOpen = true; //флаг окно созданно
            subWindow.addCloseListener(e -> {
                insertAllMyEvent(); //обновляем таблицу после закрытия окна
                windowsOpen = false; //флаг окно закрыто
                System.out.println(">> Editor Event close");
            });
        }
    }

    private Grid<MyEvent> makeATable() { //метод по созданию таблицы
        Grid<MyEvent> grid = new Grid<>();
        grid.addColumn(MyEvent::getId).setCaption("Id");
        grid.addColumn(MyEvent::getName).setCaption("Name");
        grid.addColumn(MyEvent::getDate).setCaption("Date");
        grid.addColumn(MyEvent::getCity).setCaption("City");
        grid.addColumn(MyEvent::getBuilding).setCaption("Building");

        return grid; //ожидаем озданную таблицу на странице
    }

    private void insertAllMyEvent() { //метод по заполнению таблицы данными из бд
        System.out.println(">> Fill the table with events");
        List<MyEvent> list = new MyEventDAOImpl().findAllMyEvent(); //получить все события
        grid1.setItems(list); //все события из бд записываем в таблицу
    }

    private void deleteMyEvent(MyEvent myEvent) { //метод по удалению выбранного события
        System.out.println(">> Deleting events from id " + myEvent.getId());
        new MyEventDAOImpl().deleteByIdMyEvent(myEvent.getId());
    }

}
