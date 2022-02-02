package org.example;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.example.entity.MyEvent;

import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        button.addClickListener(e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!"));
        });


        layout.addComponents(name, button,demoPanel());
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }


    /*private Grid<MyEvent> myGridVersionOne(){ //создаем таблицу с событиями
        Grid<MyEvent> grid = new Grid<>();
        //grid.setItems(list);
        //grid.addColumn(MyEvent::getName).setCaption("Name");
        //grid.addColumn(MyEvent::getDate).setCaption("Date");
        //grid.addColumn(MyEvent::getCity).setCaption("City");
        //grid.addColumn(MyEvent::getBuilding).setCaption("Building");
        return grid;
    }*/

    private Panel demoPanel(){ //форма для входа
        Panel panel = new Panel("Log in");

        FormLayout formLayout = new FormLayout();
        TextField tf1 = new TextField("User Name"); //создаем поле для ввода
        tf1.setIcon(VaadinIcons.USER); //вешаем значек
        tf1.setRequiredIndicatorVisible(true);

        PasswordField passwordField = new PasswordField("Password"); //поле для пароля
        passwordField.setIcon(VaadinIcons.LOCK);
        passwordField.setRequiredIndicatorVisible(true);

        Button button = new Button("OK"); //кнопка для подтверждения

        formLayout.addComponents(tf1,passwordField,button); //добавляем все компоненты
        panel.setContent(formLayout); //заварачивам все в панель

        return panel;
    }

}
