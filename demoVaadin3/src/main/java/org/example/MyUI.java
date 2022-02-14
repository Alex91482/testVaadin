package org.example;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import lombok.Getter;
import lombok.Setter;

import org.example.util.init.Init;
import org.example.views.GridView;
import org.example.views.LoginView;


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

        Init init1 = Init.getInstance(); //создание таблиц

        LoginView loginView = new LoginView();
        GridView gridView = new GridView();

        Navigator navigator = new Navigator(this,this);
        navigator.addView("",loginView);
        navigator.addView("grid",gridView);
        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                if(event.getNewView() instanceof GridView
                        && ((MyUI)UI.getCurrent()).getLoggedInUser() == 0){
                    //если пользователь не прошел авторизацию переход на страницу с таблицей не произойдет
                    //проверка проходит по полю из класса MyUI
                    Notification.show("Permission denied", Notification.Type.ERROR_MESSAGE);
                    return false;
                }else{
                    return true;
                }
            }
        });

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    @Getter @Setter
    private long loggedInUser; //данное поле используется для сохранения авторизации, при обновлении страницы авторизация сбрасывается
}
