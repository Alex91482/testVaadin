package org.example.entity;

import lombok.Getter;
import lombok.Setter;

public class BinderEntity {

    @Getter @Setter
    private String login;

    @Getter @Setter
    private String password;

    public BinderEntity(String login, String password){
        this.login = login;
        this.password = password;
    }
}
