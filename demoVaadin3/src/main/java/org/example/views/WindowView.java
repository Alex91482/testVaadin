package org.example.views;


import com.vaadin.data.HasValue;
import com.vaadin.ui.*;
import org.example.entity.MyEvent;
import org.example.util.jdbc.dao.MyEventDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowView {

    private final Logger logger = LoggerFactory.getLogger(WindowView.class);

    private Window subWindow;

    private TextField tfName; //текстовое поле для окна создания/перезаписи события
    private TextField tfDate; //текстовое поле для окна создания/перезаписи события
    private TextField tfCity; //текстовое поле для окна создания/перезаписи события
    private TextField tfBuilding; //текстовое поле для окна создания/перезаписи события

    private Button buttonSave;

    public Window window( MyEvent myEvent){ //окно по редактированию или созданию MyEvent

        subWindow = new Window("Event option");
        VerticalLayout subContent = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Fill in All Fields"); //строка в окне для подсказки пользователю

        tfName = new TextField("Name");
        tfName.addValueChangeListener(new ValidationCheckListener());
        tfDate = new TextField("Date");
        tfDate.addValueChangeListener(new ValidationCheckListener());
        tfCity = new TextField("City");
        tfCity.addValueChangeListener(new ValidationCheckListener());
        tfBuilding = new TextField("Building");
        tfBuilding.addValueChangeListener(new ValidationCheckListener());

        buttonSave = new Button("Save");
        buttonSave.setEnabled(false);
        buttonSave.addClickListener(click ->{
            saveOrUpdate(myEvent); //
            subWindow.close();
        });
        Button buttonCancel = new Button("Cancel");
        buttonCancel.addClickListener(click -> {
            subWindow.close();
        });

        if(myEvent != null){
            //если в метод передали событие а не null то требуется заполнить поля для ввода/редактирования данными
            //из выбранного события
            init(myEvent); //метод заполнения полей
        }

        //собираем все элементы вместе
        horizontalLayout.addComponents(buttonSave, buttonCancel);
        subContent.addComponents(label, tfName, tfDate, tfCity, tfBuilding, horizontalLayout);
        subWindow.setContent(subContent);
        subWindow.center();
        return subWindow; //ожидаем окно с полями для ввода данных и двумя кнопками сохранить, отменить
    }

    private void saveOrUpdate(MyEvent myEvent){
        if(myEvent == null){
            createMyEvent();
        }else{
            updateMyEvent(myEvent);
        }
    }

    private void createMyEvent(){ //метод по сохранению события в бд
        logger.info(">> Save new MyEvent in db");
        new MyEventDAOImpl().saveMyEvent(new MyEvent(0, tfName.getValue(), tfDate.getValue(),
                tfCity.getValue(), tfBuilding.getValue()));
    }

    private void init (MyEvent event){ //метод заполняет поля в окне для редактирования
        tfName.setValue(event.getName());
        tfDate.setValue(event.getDate());
        tfCity.setValue(event.getCity());
        tfBuilding.setValue(event.getBuilding());
    }

    private void updateMyEvent(MyEvent myEvent){ //метод по обновлению события
        logger.info(">> Update MyEvent id = " + myEvent.getId());
        myEvent.setName(tfName.getValue());
        myEvent.setDate(tfDate.getValue());
        myEvent.setCity(tfCity.getValue());
        myEvent.setBuilding(tfBuilding.getValue());
        new MyEventDAOImpl().updateMyEvent(myEvent);
    }

    private class ValidationCheckListener implements HasValue.ValueChangeListener {
        //все ли поля заполнены, если да то разрешить сохранение
        @Override
        public void valueChange(HasValue.ValueChangeEvent event) {

            if(!tfName.getValue().isEmpty() && !tfDate.getValue().isEmpty()
                    && !tfCity.getValue().isEmpty() && !tfBuilding.getValue().isEmpty()
                    && validationStr(tfName.getValue(), tfDate.getValue(),
                    tfCity.getValue(), tfBuilding.getValue())){
                buttonSave.setEnabled(true);
            }else{
                buttonSave.setEnabled(false);
            }
        }
    }

    private boolean validationStr(String name, String date, String city, String building){
        //метод удаляет все пробелы в начале и конце строки
        int nameLength = name.trim().length();
        int dateLength = date.trim().length();
        int cityLength = city.trim().length();
        int buildingLength = building.trim().length();

        if(nameLength > 0 && dateLength > 0 &&
            cityLength > 0 && buildingLength > 0){
            return true; //во всех строках содержится хотя бы по одному символу
        }
        return false; //если хотя бы одна из строк не содержит какие либо данные а пробелы то валидация не пройденна
    }
}
