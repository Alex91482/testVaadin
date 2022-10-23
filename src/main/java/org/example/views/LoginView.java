package org.example.views;


import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;
import org.example.MyUI;
import org.example.entity.BinderEntity;
import org.example.entity.MyAccount;
import org.example.util.jdbc.dao.MyAccountDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginView extends VerticalLayout implements View {

    private final Logger logger = LoggerFactory.getLogger(LoginView.class);
    private final Binder<BinderEntity> binder = new Binder<>();
    private TextField tf1;
    private PasswordField passwordField;
    private Label loginStatus;
    private Label passwordStatus;

    public LoginView() {
        demoPanel(this);
    }

    private void demoPanel(VerticalLayout verticalLayout){ //форма для входа

        Label label1 = new Label("Enter login and password");

        loginStatus = new Label();
        loginStatus.setStyleName("red");

        tf1 = new TextField("User Name"); //создаем поле для ввода
        tf1.setIcon(VaadinIcons.USER); //вешаем значек
        tf1.setRequiredIndicatorVisible(true);

        passwordStatus = new Label();
        passwordField = new PasswordField("Password"); //поле для пароля
        passwordField.setIcon(VaadinIcons.LOCK);
        passwordField.setRequiredIndicatorVisible(true);

        binderInit();

        Button button = new Button("OK"); //кнопка для подтверждения
        button.addClickListener(event -> {
            try {
                if (!tf1.getValue().equals("") && !passwordField.getValue().equals("")) {
                    //если логин и пароль не пустые то делаем запрос в бд
                    setStatusTextField(false);
                    MyAccount myAccount = new MyAccountDAOImpl().findMyAccount(tf1.getValue(), passwordField.getValue());
                    if (myAccount.getUserName() != null && !myAccount.getUserName().equals("")) {
                        //проверяем поле username
                        ((MyUI)UI.getCurrent()).setLoggedInUser(myAccount.getId()); //добавляем id авторизованного пользователя
                        this.getUI().getNavigator().navigateTo("grid");
                    } else {
                        label1.setValue("User " + tf1.getValue() + " is not found");
                    }
                } else {
                    label1.setValue("Username and Password fields must be filled");
                    setStatusTextField(true);
                    binder.validate();
                }
            }catch (Exception e){
               logger.error(e.getMessage());
            }
        });

        verticalLayout.addComponents(label1,loginStatus,tf1,passwordStatus,passwordField,button); //заварачивам все в панель
        verticalLayout.setComponentAlignment(label1, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(loginStatus, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(tf1, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(passwordStatus, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
    }

    private void setStatusTextField(Boolean flag){
        if(flag){
            tf1.setComponentError(new UserError("Length cannot be zero"));
            passwordField.setComponentError(new UserError("Password length cannot be zero"));
        }else{
            tf1.setComponentError(null);
            passwordField.setComponentError(null);
        }
    }

    private void binderInit(){
        binder.forField(tf1)
                .withValidator(login ->
                        login == null || login.length() > 0, "Length cannot be zero"
                )
                .withValidationStatusHandler(status -> {
                    loginStatus.setValue(status.getMessage().orElse(""));
                    loginStatus.setVisible(status.isError());
                })
                .bind(BinderEntity::getLogin, BinderEntity::setLogin);
        binder.forField(passwordField)
                .withValidator(password ->
                        password == null || password.length() > 0, "Password length cannot be zero"
                )
                .withValidationStatusHandler(status -> {
                    passwordStatus.setValue(status.getMessage().orElse(""));
                    passwordStatus.setVisible(status.isError());
                })
                .bind(BinderEntity::getPassword, BinderEntity::setPassword);
    }
}