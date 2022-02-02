package org.example.views;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;

public class LoginView extends VerticalLayout implements View {
    public LoginView() {


        demoPanel(this);
    }

    private void demoPanel(VerticalLayout verticalLayout){ //форма для входа



        TextField tf1 = new TextField("User Name"); //создаем поле для ввода
        tf1.setIcon(VaadinIcons.USER); //вешаем значек
        tf1.setRequiredIndicatorVisible(true);

        PasswordField passwordField = new PasswordField("Password"); //поле для пароля
        passwordField.setIcon(VaadinIcons.LOCK);
        passwordField.setRequiredIndicatorVisible(true);

        Button button = new Button("OK"); //кнопка для подтверждения
        button.addClickListener(event -> {
            this.getUI().getNavigator().navigateTo("grid");
        });
        verticalLayout.addComponents(tf1,passwordField,button); //заварачивам все в панель
        verticalLayout.setComponentAlignment(tf1, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);

    }

}

/*final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        button.addClickListener(e -> {
            layout.addComponent(new Label("Thanks " + name.getValue()
                    + ", it works!"));
        });*/