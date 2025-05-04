package com.mycompany.lb4;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {}

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void connect() throws SQLException {
        String url = "jdbc:postgresql://192.168.1.66:2345/OliwanderShop";
        String user = "postgres";
        String password = "88665511";

        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Подключено к базе данных.");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Отключено от базы данных.");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void clearAllData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET CONSTRAINTS ALL DEFERRED");

            stmt.execute("DELETE FROM Sale");
            stmt.execute("DELETE FROM Buyer");
            stmt.execute("DELETE FROM MagicStick");
            stmt.execute("DELETE FROM DeliveryDetails");
            stmt.execute("DELETE FROM Delivery");

            stmt.execute("UPDATE Wood SET amount = 0");
            stmt.execute("UPDATE Core SET amount = 0");

            stmt.execute("ALTER SEQUENCE sale_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE wood_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE core_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE magicstick_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE buyer_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE delivery_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE deliverydetails_id_seq RESTART WITH 1");

            System.out.println("Все данные удалены. Остатки на складе обнулены.");
        }
    }
}