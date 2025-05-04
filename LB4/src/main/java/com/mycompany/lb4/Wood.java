package com.mycompany.lb4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Wood {
    private int id;
    private String type;
    private int amount;

    public Wood(int id, String type, int amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void save() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        if (this.id == 0) {
            String sql = "INSERT INTO Wood (type, amount) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, type);
                pstmt.setInt(2, amount);
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        } else {
            String sql = "UPDATE Wood SET type = ?, amount = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, type);
                pstmt.setInt(2, amount);
                pstmt.setInt(3, id);
                pstmt.executeUpdate();
            }
        }
    }

    public static Wood getById(int id) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM Wood WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Wood(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getInt("amount")
                );
            }
        }
        return null;
    }

    public static List<Wood> getAll() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Wood> woods = new ArrayList<>();
        String sql = "SELECT * FROM Wood ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                woods.add(new Wood(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getInt("amount")
                ));
            }
        }
        return woods;
    }

    public void updateAmount(int addedAmount) throws SQLException {
        this.amount += addedAmount;
        this.save();
    }

     @Override
    public String toString() {
        return String.format("%s (Есть в наличии: %d)", type, amount);
    }
}