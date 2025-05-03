package com.mycompany.lb4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Delivery {
    private int id;
    private LocalDate deliveryDate;
    private List<DeliveryDetails> details;

    public Delivery(int id, LocalDate deliveryDate) {
        this.id = id;
        this.deliveryDate = deliveryDate;
        this.details = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public List<DeliveryDetails> getDetails() {
        return details;
    }

    public void save() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        if (this.id == 0) {
            String sql = "INSERT INTO Delivery (delivery_date) VALUES (?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setDate(1, Date.valueOf(deliveryDate));
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        } else {
            String sql = "UPDATE Delivery SET delivery_date = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setDate(1, Date.valueOf(deliveryDate));
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            }
        }
    }

    public void addDetail(DeliveryDetails detail) throws SQLException {
        detail.setDeliveryId(this.id);
        detail.save();
        this.details.add(detail);
    }

    public static List<Delivery> getAll() throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM Delivery ORDER BY delivery_date DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Delivery delivery = new Delivery(
                        rs.getInt("id"),
                        rs.getDate("delivery_date").toLocalDate()
                );
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }

    public static Delivery getById(int id) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM Delivery WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Delivery delivery = new Delivery(
                        rs.getInt("id"),
                        rs.getDate("delivery_date").toLocalDate()
                );
                delivery.details = DeliveryDetails.getByDeliveryId(id);
                return delivery;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Поставка №" + id + ", дата: " + deliveryDate;
    }
}