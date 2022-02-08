package org.example.util.jdbc.dao;

import org.example.entity.MyAccount;

public interface MyAccountDAO {
    void saveMyAccount(String userName, String password) throws Exception;
    MyAccount findMyAccount(String userName, String password) throws Exception;
    void createTableMyAccount() throws Exception;
}
