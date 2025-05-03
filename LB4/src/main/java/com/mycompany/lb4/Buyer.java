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

    public Buyer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        if (this.id == 0) {
            // Вставка новой записи
            String sql = "INSERT INTO Buyer (name) VALUES (?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, name);
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        } else {
            // Обновление существующей записи
            String sql = "UPDATE Buyer SET name = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            }
        }
    }

    public static Buyer getById(int id) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM Buyer WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Buyer(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
        }
        return null;
    }

    public static List<Buyer> getAll() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Buyer> buyers = new ArrayList<>();
        String sql = "SELECT * FROM Buyer ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                buyers.add(new Buyer(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        }
        return buyers;
    }

    public static Buyer getByName(String name) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM Buyer WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Buyer(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Buyer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}