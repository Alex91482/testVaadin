package org.example.util.init;

import org.example.util.jdbc.ConnectionUtil;
import org.example.util.jdbc.dao.MyAccountDAO;

import java.sql.Connection;

public class Init {

    public static void main(String[]args) throws Exception{
        ConnectionUtil connectionUtil = new ConnectionUtil();
        Connection oneConnection = connectionUtil.getMyH2Connection();

        MyAccountDAO myAccountDAO = new MyAccountDAO();
        myAccountDAO.createTableMyAccount(oneConnection);
        myAccountDAO.saveAccount(oneConnection,"admin","admin");

        connectionUtil.closeQuietly(oneConnection);

        System.out.println("Th sleep");
        Thread.sleep(48000);
        System.out.println("end");
    }
}
