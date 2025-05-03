package com.mycompany.lb4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDetails {
    private int id;
    private int deliveryId;
    private String componentType;
    private int componentId;
    private int amount;

    public DeliveryDetails(int id, int deliveryId, String componentType, int componentId, int amount) {
        this.id = id;
        this.deliveryId = deliveryId;
        this.componentType = componentType;
        this.componentId = componentId;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getDeliveryId() {
        return deliveryId;
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

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void save() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        if (this.id == 0) {
            String sql = "INSERT INTO DeliveryDetails (delivery_id, component_type, component_id, amount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, deliveryId);
                pstmt.setString(2, componentType);
                pstmt.setInt(3, componentId);
                pstmt.setInt(4, amount);
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        } else {
            String sql = "UPDATE DeliveryDetails SET delivery_id = ?, component_type = ?, component_id = ?, amount = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, deliveryId);
                pstmt.setString(2, componentType);
                pstmt.setInt(3, componentId);
                pstmt.setInt(4, amount);
                pstmt.setInt(5, id);
                pstmt.executeUpdate();
            }
        }
    }

    public static DeliveryDetails getById(int id) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM DeliveryDetails WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new DeliveryDetails(
                        rs.getInt("id"),
                        rs.getInt("delivery_id"),
                        rs.getString("component_type"),
                        rs.getInt("component_id"),
                        rs.getInt("amount")
                );
            }
        }
        return null;
    }

    public static List<DeliveryDetails> getByDeliveryId(int deliveryId) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<DeliveryDetails> detailsList = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryDetails WHERE delivery_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, deliveryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                detailsList.add(new DeliveryDetails(
                        rs.getInt("id"),
                        rs.getInt("delivery_id"),
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
        return String.format("DeliveryDetails{id=%d, deliveryId=%d, type='%s', componentId=%d, amount=%d}",
                id, deliveryId, componentType, componentId, amount);
    }
}