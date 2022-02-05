package org.example.views;

import com.vaadin.ui.*;
import org.example.entity.MyEvent;
import org.example.util.jdbc.dao.MyEventDAOImpl;

public class WindowView {

    private Window subWindow;
    private Label label; //строка в окне для подсказке пользователю

    private TextField tfName; //текстовое поле для окна создания/перезаписи события
    private TextField tfDate; //текстовое поле для окна создания/перезаписи события
    private TextField tfCity; //текстовое поле для окна создания/перезаписи события
    private TextField tfBuilding; //текстовое поле для окна создания/перезаписи события

    public Window makeAWindow(String value, MyEvent myEvent){ //окно по редактированию или созданию MyEvent

        subWindow = new Window("Event option");
        VerticalLayout subContent = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        label = new Label("Fill in All Fields");

        tfName = new TextField("Name");
        tfDate = new TextField("Date");
        tfCity = new TextField("City");
        tfBuilding = new TextField("Building");

        Button buttonSave = new Button("Save");

        //требуется определить нужно ли заполнять поля существующими данными
        //и требуется ли передавать id для обновления
        //для этого используем переданное значение value
        switch (value){
            case ("create") : //получили вызов создать
                buttonSaveCreate(buttonSave);
                break;
            case ("edit") : //получили вызов обновить
                buttonEditCreate(buttonSave, myEvent);
                break;
        }

        Button buttonCancel = new Button("Cancel");
        buttonCancel.addClickListener(click -> {
            subWindow.close();
        });

        //собираем все элементы вместе
        horizontalLayout.addComponents(buttonSave,buttonCancel);
        subContent.addComponents(label,tfName,tfDate,tfCity,tfBuilding,horizontalLayout);
        subWindow.setContent(subContent);
        // Center it in the browser window
        subWindow.center();

        return subWindow; //ожидаем окно с полями для ввода данных и двумя кнопками сохранить, отменить
    }

    private void buttonSaveCreate(Button button){ //когда требуется сохранить новое событие
        button.addClickListener(click ->{
            //проверяем поля там должно быть хоть что то
            if(!tfName.getValue().isEmpty() && !tfDate.getValue().isEmpty() &&
                    !tfCity.getValue().isEmpty() && !tfBuilding.getValue().isEmpty()){
                //если поставить пробелы в полях то валидация  пройдет :-/
                // equals то же проходит если поставить два пробела
                String name = tfName.getValue();
                String date = tfDate.getValue();
                String city = tfCity.getValue();
                String building = tfBuilding.getValue();
                System.out.println(">> Save event: name = " + name + " date =  " + date + " city = "
                        + city + " building = " + building);
                createMyEvent(new MyEvent().builder()
                        .id(1L) //можно задать любое действительное значение (оно будет проигнорированно при сохранении)
                        //и требуется в данном конкретном случае тоько для функционирования билдера
                        .name(name)
                        .date(date)
                        .city(city)
                        .building(building)
                        .build()
                );
                subWindow.close(); //закрываем окно
            }else {
                label.setValue("Pleas Fill All Fields"); //если какое либо из полей не заполнена меняем текст
            }
        });
    }

    private void buttonEditCreate(Button button, MyEvent myEvent){ //когда требуется обновить существующие событие
        if(myEvent.getId() != 0 && myEvent.getName() != null && !myEvent.getName().equals("")){
            //в H2 автоинкремент начинается с 1
            //перестраховываемся и проверяем пустое ли название события

            tfName.setValue(myEvent.getName());
            tfDate.setValue(myEvent.getDate());
            tfCity.setValue(myEvent.getCity());
            tfBuilding.setValue(myEvent.getBuilding());

            button.addClickListener(click ->{
                //проверяем поля там должно быть хоть что то
                if(!tfName.getValue().isEmpty() && !tfDate.getValue().isEmpty() &&
                        !tfCity.getValue().isEmpty() && !tfBuilding.getValue().isEmpty()) {
                    //если поставить пробелы в полях то валидация  пройдет :-/
                    // equals то же проходит если поставить два пробела
                    String name = tfName.getValue();
                    String date = tfDate.getValue();
                    String city = tfCity.getValue();
                    String building = tfBuilding.getValue();
                    System.out.println(">> Save event: name = " + name + " date =  " + date + " city = "
                            + city + " building = " + building);
                    updateMyEvent(new MyEvent().builder()
                            .id(myEvent.getId()) //требуется существующее значение id для метода обновления
                            .name(name)
                            .date(date)
                            .city(city)
                            .building(building)
                            .build()
                    );
                    subWindow.close(); //закрываем окно
                }
                else {
                    label.setValue("Pleas Fill All Fields"); //если какое либо из полей не заполнена меняем текст
                }
            });
        }else{
            buttonSaveCreate(button); //если экземпляр MyEvent пустой тогда создаем слушателя на обычное сохранение
        }
    }

    private void createMyEvent(MyEvent myEvent){ //метод по сохранению события в бд
        System.out.println(">> Save new MyEvent in db");
        new MyEventDAOImpl().saveMyEvent(myEvent);
    }

    private void updateMyEvent(MyEvent myEvent){
        System.out.println(">> Update MyEvent id = " + myEvent.getId());
        new MyEventDAOImpl().updateMyEvent(myEvent);
    }
}
