package org.example.views;

import com.vaadin.data.provider.Query;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.example.MyUI;
import org.example.entity.MyEvent;
import org.example.service.xls.XlsService;
import org.example.util.jdbc.dao.MyEventDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GridView extends VerticalLayout implements View {

    private final Logger logger = LoggerFactory.getLogger(GridView.class);

    private Grid<MyEvent> grid1;
    private boolean windowsOpen = false; //флаг открыто ли окно для изменения/добавления MyEvent

    private Button buttonCreate;
    private Button buttonCreateDocument;
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
        buttonCreate.addStyleName("myCreate"); //изменение цвета текста кнопки
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
            //вызываем функцию проверки состояния окна и передаем в нее событие выбранное в таблице
            checkExists(grid1.getSelectedItems().stream().findFirst().get());
        });
        buttonCreateDocument = new Button("Create Document");
        buttonCreateDocument.addStyleName("myCreate"); //изменение цвета текста кнопки
        buttonCreateDocument.addClickListener(event -> {
            //данные для отчета берем из таблицы и передаем в метод который вызовет класс отвечающей за создание отчетов
            createDocXls(grid1.getDataProvider()
                    .fetch(new Query<>()).collect(Collectors.toList()));

        });
        horizontalLayout.addComponents(buttonCreate, buttonDelete, buttonEdit, buttonCreateDocument);

        this.addComponents(grid1, horizontalLayout);
    }


    private void checkExists(MyEvent myEvent) { //метод по проверки того существует ли окно
        if (!windowsOpen) { //если флаг false то создаем окно
            //на момент открытия окна блокируем кнопку удаления и создания отчета
            //после закрытия окна выделение со строки снмается так что кнопка delete разблокируется при выборе строки
            buttonDelete.setEnabled(false);
            buttonCreateDocument.setEnabled(false);
            logger.info(">> Editor Event open");

            Window subWindow = new WindowView().window(myEvent);

            MyUI.getCurrent().addWindow(subWindow); //добавляем окно на страницу
            windowsOpen = true; //флаг окно созданно
            subWindow.addCloseListener(e -> {
                insertAllMyEvent(); //обновляем таблицу после закрытия окна
                windowsOpen = false; //флаг окно закрыто
                buttonCreateDocument.setEnabled(true); //разблокируем кнопку создания отчета
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

        return grid; //ожидаем созданную таблицу
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

    private void createDocXls(List<MyEvent> myEventList){ //метод по созданию файла xls в папке temp
        try{
            boolean createReport = new XlsService().createXlsFile(myEventList);
            if(createReport){
                logger.info(">> Report created");
                new Notification("Info", "Xls report created",
                        Notification.Type.HUMANIZED_MESSAGE).show(Page.getCurrent()); //всплывающее окно с объявлением что отчет создан
            }else{
                logger.warn(">> Report not created");
                new Notification("Warn", "Failed to generate report",
                        Notification.Type.WARNING_MESSAGE).show(Page.getCurrent()); //всплывающее окно с объявлением что отчет не создан
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            new Notification("Error", "An error occurred while generating the report",
                    Notification.Type.ERROR_MESSAGE).show(Page.getCurrent()); //всплывающее окно с объявлением о том что отчет не создан
        }
    } //ожидаем что отчет будет создан либо получиненно какое либо сообщение о неудаче
}
