package org.example;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.example.entity.MyEvent;
import org.example.views.GridView;
import org.example.views.LoginView;

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
        LoginView loginView = new LoginView();
        GridView gridView = new GridView();

        Navigator navigator = new Navigator(this,this);
        navigator.addView("",loginView);
        navigator.addView("grid",gridView);

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
    //получить все, получить по id

}
