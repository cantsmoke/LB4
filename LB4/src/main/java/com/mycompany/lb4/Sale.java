package com.mycompany.lb4;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private int id;
    private Buyer buyer;
    private MagicStick stick;
    private LocalDate saleDate;

    public Sale(int id, Buyer buyer, MagicStick stick, LocalDate saleDate) {
        this.id = id;
        this.buyer = buyer;
        this.stick = stick;
        this.saleDate = saleDate;
    }

    public int getId() {
        return id;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public MagicStick getStick() {
        return stick;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void save() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        if (this.id == 0) {
            String sql = "INSERT INTO Sale (buyer_id, stick_id, sale_date) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                if (buyer == null || stick == null || saleDate == null) {
                    throw new IllegalArgumentException("Необходимо указать покупателя, палочку и дату продажи.");
                }

                pstmt.setInt(1, buyer.getId());
                pstmt.setInt(2, stick.getId());
                pstmt.setDate(3, Date.valueOf(saleDate));
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        } else {
            String sql = "UPDATE Sale SET buyer_id = ?, stick_id = ?, sale_date = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                if (buyer == null || stick == null || saleDate == null) {
                    throw new IllegalArgumentException("Необходимо указать покупателя, палочку и дату продажи.");
                }

                pstmt.setInt(1, buyer.getId());
                pstmt.setInt(2, stick.getId());
                pstmt.setDate(3, Date.valueOf(saleDate));
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
            }
        }
    }

    public static Sale getById(int id) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM Sale WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Buyer buyer = Buyer.getById(rs.getInt("buyer_id"));
                MagicStick stick = MagicStick.getById(rs.getInt("stick_id"));
                return new Sale(
                        rs.getInt("id"),
                        buyer,
                        stick,
                        rs.getDate("sale_date").toLocalDate()
                );
            }
        }
        return null;
    }

    public static List<Sale> getAll() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM Sale ORDER BY sale_date DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Buyer buyer = Buyer.getById(rs.getInt("buyer_id"));
                MagicStick stick = MagicStick.getById(rs.getInt("stick_id"));
                sales.add(new Sale(
                        rs.getInt("id"),
                        buyer,
                        stick,
                        rs.getDate("sale_date").toLocalDate()
                ));
            }
        }
        return sales;
    }

    public static boolean isStickSold(int stickId) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM Sale WHERE stick_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, stickId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public static List<Sale> getByBuyer(Buyer buyer) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM Sale WHERE buyer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, buyer.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MagicStick stick = MagicStick.getById(rs.getInt("stick_id"));
                sales.add(new Sale(
                        rs.getInt("id"),
                        buyer,
                        stick,
                        rs.getDate("sale_date").toLocalDate()
                ));
            }
        }
        return sales;
    }

    public static List<Sale> getByStick(MagicStick stick) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM Sale WHERE stick_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, stick.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Buyer buyer = Buyer.getById(rs.getInt("buyer_id"));
                sales.add(new Sale(
                        rs.getInt("id"),
                        buyer,
                        stick,
                        rs.getDate("sale_date").toLocalDate()
                ));
            }
        }
        return sales;
    }

    @Override
    public String toString() {
        return "Продажа ID: " + id + " | Покупатель: " + buyer.getName() +
               " | Палочка: " + stick.getId() + " | Дата: " + saleDate;
    }
}