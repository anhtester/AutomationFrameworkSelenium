/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelpers {

    public DatabaseHelpers() {
        super();
    }

    public static Connection getMySQLConnection(String hostName, String dbName, String userName, String password) throws SQLException {

        // Ví dụ: jdbc:mysql://localhost:3306/saleserp
        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

        Connection conn = DriverManager.getConnection(connectionURL, userName, password);

        return conn;
    }

}
