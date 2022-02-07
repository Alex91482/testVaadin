package org.example.views;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import org.example.entity.MyAccount;
import org.example.util.jdbc.dao.MyAccountDAOImpl;

public class LoginView extends VerticalLayout implements View {
    public LoginView() {

        demoPanel(this);
    }

    private void demoPanel(VerticalLayout verticalLayout){ //форма для входа
        //данную "регистрацию" легко обойти если есть ссылка на следующую страницу
        //например на http://localhost:8080/#!grid

        Label label1 = new Label("Enter login and password");

        TextField tf1 = new TextField("User Name"); //создаем поле для ввода
        tf1.setIcon(VaadinIcons.USER); //вешаем значек
        tf1.setRequiredIndicatorVisible(true);

        PasswordField passwordField = new PasswordField("Password"); //поле для пароля
        passwordField.setIcon(VaadinIcons.LOCK);
        passwordField.setRequiredIndicatorVisible(true);

        Button button = new Button("OK"); //кнопка для подтверждения
        button.addClickListener(event -> {
            if(!tf1.getValue().equals("") && !passwordField.getValue().equals("")){
                //если логин и пароль не пустые то делаем запрос в бд
                MyAccount myAccount = new MyAccountDAOImpl().findMyAccount(tf1.getValue(),passwordField.getValue());
                if(myAccount.getUserName() != null && !myAccount.getUserName().equals("")){
                    //проверяем поле username
                    this.getUI().getNavigator().navigateTo("grid");
                }else{
                    label1.setValue("User " + tf1.getValue() + " is not found");
                }
            }else{
                label1.setValue("Username and Password fields must be filled");
            }
        });

        verticalLayout.addComponents(label1,tf1,passwordField,button); //заварачивам все в панель
        verticalLayout.setComponentAlignment(label1, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(tf1, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
    }
}