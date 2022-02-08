package org.example.views;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.example.MyUI;
import org.example.entity.MyEvent;
import org.example.util.jdbc.dao.MyEventDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GridView extends VerticalLayout implements View {

    private final Logger logger = LoggerFactory.getLogger(GridView.class);

    private Grid<MyEvent> grid1;
    private boolean windowsOpen = false; //флаг открыто ли окно для изменения/добавления MyEvent

    private Button buttonCreate;
    private Button buttonDelete;
    private Button buttonEdit;

    public GridView() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(); //часть с кнопками
        grid1 = makeATable(); //создаем таблицу
        insertAllMyEvent(); //заполнение данными из бд
        grid1.setSizeFull(); //таблица на весь экран
        grid1.setSelectionMode(Grid.SelectionMode.SINGLE); //модефикация таблицы для возможности выбора строки в таблице

        grid1.addItemClickListener(event -> { //слушатель на двойной клик
            if (event.getMouseEventDetails().isDoubleClick()) {
                checkExists(event.getItem());
            }
        });
        grid1.addSelectionListener(event -> { //слушатель на выбор строки в таблице
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
            //вызываем функцию проверки состояния окна и передаем null т.к. вызываются методы сохранения нового события
            checkExists(null);
        });
        buttonDelete = new Button("Delete");
        buttonDelete.addStyleName(ValoTheme.BUTTON_DANGER); //изменение цвета кнопки на красный
        //по умолчанию заблокированно (разблокируется методом выше grid1.addSelectionListener)
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
            //на момент открытия окна блокируем кнопку удаления
            //после закрытия окна выделение со строки снмается так что кнопка разблокируется при выборе строки
            buttonDelete.setEnabled(false);
            logger.info(">> Editor Event open");

            Window subWindow = new WindowView().window(myEvent);

            MyUI.getCurrent().addWindow(subWindow); //добавляем окно на страницу
            windowsOpen = true; //флаг окно созданно
            subWindow.addCloseListener(e -> {
                insertAllMyEvent(); //обновляем таблицу после закрытия окна
                windowsOpen = false; //флаг окно закрыто
                logger.info(">> Editor Event close");
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
        try {
            logger.info(">> Fill the table with events");
            List<MyEvent> list = new MyEventDAOImpl().findAllMyEvent(); //получить все события
            grid1.setItems(list); //все события из бд записываем в таблицу
        }catch (Exception e){
            logger.error(e.getMessage());
            new Notification("Error", "Error filling table with data",
                    Notification.Type.ERROR_MESSAGE).show(Page.getCurrent()); //всплывающее окно с объявлением пользователю о ошибке
        }
    }

    private void deleteMyEvent(MyEvent myEvent) { //метод по удалению выбранного события
        try {
            logger.info(">> Deleting events from id " + myEvent.getId());
            new MyEventDAOImpl().deleteByIdMyEvent(myEvent.getId());
        }catch (Exception e){
            logger.error(e.getMessage());
            new Notification("Error", "Error when deleting selected event",
                    Notification.Type.ERROR_MESSAGE).show(Page.getCurrent()); //всплывающее окно с объявлением пользователю о ошибке
        }
    }

}
