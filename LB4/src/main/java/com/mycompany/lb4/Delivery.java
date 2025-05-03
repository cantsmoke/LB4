/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Delivery {
    private int id;
    private List<DeliveryDetails> details;
    private LocalDate deliveryDate;

    // Конструктор
    public Delivery(int id) throws SQLException {
        this.id = id;
        this.details = new ArrayList<>();
        loadDetails();
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public List<DeliveryDetails> getDetails() {
        return details;
    }
    
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    // Загрузка деталей поставки из БД
    private void loadDetails() throws SQLException {
        this.details = DeliveryDetails.getByDeliveryId(this.id);
    }

    // Сохранить новую поставку в БД
    public void save() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        if (this.id == 0) {
            String sql = "INSERT INTO Delivery (details_id, delivery_date) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                // Вставка пустой записи для генерации ID
                pstmt.setNull(1, Types.INTEGER);
                pstmt.setDate(2, Date.valueOf(deliveryDate));
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    // Добавить деталь поставки
    public void addDetails(DeliveryDetails detail) throws SQLException {
        detail.save(); // сохраняем деталь в БД
        this.details.add(detail);
    }

    // Получить поставку по ID
    public static Delivery getById(int id) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM Delivery WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Delivery(id);
            }
        }
        return null;
    }

    // Получить все поставки
    public static List<Delivery> getAll() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM Delivery ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                deliveries.add(new Delivery(rs.getInt("id")));
            }
        }
        return deliveries;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", detailsCount=" + details.size() +
                '}';
    }
}