package com.projetopessoal.db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            if (conn == null) {
                Properties properties = DB.loadProperties();
                conn = DriverManager.getConnection(properties.getProperty("dburl"), properties);
            }
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream file = new FileInputStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(file);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
