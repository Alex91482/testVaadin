package org.example.util.jdbc.dao;

import org.example.entity.MyAccount;

public interface MyAccountDAO {
    void saveMyAccount(String userName, String password);
    MyAccount findMyAccount(String userName, String password);
    void createTableMyAccount();
}
