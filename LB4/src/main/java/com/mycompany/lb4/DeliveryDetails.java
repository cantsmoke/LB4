/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDetails {
    private int id;
    private String componentType; // "WOOD" или "CORE"
    private int componentId;
    private int amount;

    // Конструктор
    public DeliveryDetails(int id, String componentType, int componentId, int amount) {
        this.id = id;
        this.componentType = componentType;
        this.componentId = componentId;
        this.amount = amount;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getComponentType() {
        return componentType;
    }

    public int getComponentId() {
        return componentId;
    }

    public int getAmount() {
        return amount;
    }

    // Сеттеры
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    // Сохранить или обновить в БД
    public void save() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        if (this.id == 0) {
            // Вставка новой записи
            String sql = "INSERT INTO DeliveryDetails (component_type, component_id, amount) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, componentType);
                pstmt.setInt(2, componentId);
                pstmt.setInt(3, amount);
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        } else {
            // Обновление существующей записи
            String sql = "UPDATE DeliveryDetails SET component_type = ?, component_id = ?, amount = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, componentType);
                pstmt.setInt(2, componentId);
                pstmt.setInt(3, amount);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
            }
        }
    }

    // Получить запись по ID
    public static DeliveryDetails getById(int id) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM DeliveryDetails WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new DeliveryDetails(
                        rs.getInt("id"),
                        rs.getString("component_type"),
                        rs.getInt("component_id"),
                        rs.getInt("amount")
                );
            }
        }
        return null;
    }

    // Получить все детали поставки по delivery_id
    public static List<DeliveryDetails> getByDeliveryId(int deliveryId) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<DeliveryDetails> detailsList = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryDetails WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, deliveryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                detailsList.add(new DeliveryDetails(
                        rs.getInt("id"),
                        rs.getString("component_type"),
                        rs.getInt("component_id"),
                        rs.getInt("amount")
                ));
            }
        }
        return detailsList;
    }

    @Override
    public String toString() {
        return "DeliveryDetails{" +
                "id=" + id +
                ", componentType='" + componentType + '\'' +
                ", componentId=" + componentId +
                ", amount=" + amount +
                '}';
    }
}