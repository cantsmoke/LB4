package com.mycompany.lb4;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private boolean isBackupAvailable = false;
    private List<Wood> woodBackup = new ArrayList<>();
    private List<Core> coreBackup = new ArrayList<>();
    private List<MagicStick> stickBackup = new ArrayList<>();
    private List<Buyer> buyerBackup = new ArrayList<>();
    private List<Delivery> deliveryBackup = new ArrayList<>();
    private List<DeliveryDetails> deliveryDetailsBackup = new ArrayList<>();

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
        Statement stmt = connection.createStatement();

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

    public void backupData() throws Exception {
        woodBackup = Wood.getAll();
        coreBackup = Core.getAll();
        stickBackup = MagicStick.getAll();
        buyerBackup = Buyer.getAll();
        deliveryBackup = Delivery.getAll();
        for (Delivery d : deliveryBackup) {
            deliveryDetailsBackup.addAll(d.getDetails());
        }

        isBackupAvailable = true;
        System.out.println("Резервная копия создана.");
    }

    public void restoreData() throws Exception {
        if (!isBackupAvailable) {
            throw new IllegalStateException("Нет доступной резервной копии.");
        }

        clearAllData();

        for (Wood w : woodBackup) {
            w.setAmount(w.getAmount());
            w.save();
        }

        for (Core c : coreBackup) {
            c.setAmount(c.getAmount());
            c.save();
        }

        for (MagicStick s : stickBackup) {
            s.save();
        }

        for (Buyer b : buyerBackup) {
            b.save();
        }

        for (DeliveryDetails d : deliveryDetailsBackup) {
            d.save();
        }

        System.out.println("Данные восстановлены из резервной копии.");
    }
}