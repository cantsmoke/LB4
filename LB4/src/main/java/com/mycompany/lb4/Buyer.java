/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Buyer {
    private int id;
    private String name;
    private MagicStick stick;

    // Конструктор
    public Buyer(int id, String name, MagicStick stick) {
        this.id = id;
        this.name = name;
        this.stick = stick;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MagicStick getStick() {
        return stick;
    }

    // Сеттеры
    public void setName(String name) {
        this.name = name;
    }

    public void setStick(MagicStick stick) {
        this.stick = stick;
    }

    // Сохранить или обновить в БД
    public void save() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        if (this.id == 0) {
            // Вставка новой записи
            String sql = "INSERT INTO Buyer (name, stick_id) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, stick.getId());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        } else {
            // Обновление существующей записи
            String sql = "UPDATE Buyer SET name = ?, stick_id = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, stick.getId());
                pstmt.setInt(3, id);
                pstmt.executeUpdate();
            }
        }
    }

    // Получить покупателя по ID
    public static Buyer getById(int id) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM Buyer WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                MagicStick stick = MagicStick.getById(rs.getInt("stick_id"));
                return new Buyer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        stick
                );
            }
        }
        return null;
    }

    // Получить всех покупателей
    public static List<Buyer> getAll() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Buyer> buyers = new ArrayList<>();
        String sql = "SELECT * FROM Buyer ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MagicStick stick = MagicStick.getById(rs.getInt("stick_id"));
                buyers.add(new Buyer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        stick
                ));
            }
        }
        return buyers;
    }

    // Привязка палочки к покупателю
    public void buyStick(MagicStick stick) throws SQLException {
        this.stick = stick;
        this.save();
    }
    
    public static boolean isStickSold(int stickId) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM Buyer WHERE stick_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, stickId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stickId=" + (stick != null ? stick.getId() : "null") +
                '}';
    }
}