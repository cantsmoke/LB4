package com.mycompany.lb4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MagicStick {
    private int id;
    private Wood wood;
    private Core core;
    private double price;

    public MagicStick(int id, Wood wood, Core core, double price) {
        this.id = id;
        this.wood = wood;
        this.core = core;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Wood getWood() {
        return wood;
    }

    public Core getCore() {
        return core;
    }

    public double getPrice() {
        return price;
    }

    public void setWood(Wood wood) {
        this.wood = wood;
    }

    public void setCore(Core core) {
        this.core = core;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void save() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        if (this.id == 0) {
            String sql = "INSERT INTO MagicStick (core_id, wood_id, price) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, core.getId());
                pstmt.setInt(2, wood.getId());
                pstmt.setDouble(3, price);
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        } else {
            String sql = "UPDATE MagicStick SET core_id = ?, wood_id = ?, price = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, core.getId());
                pstmt.setInt(2, wood.getId());
                pstmt.setDouble(3, price);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
            }
        }
    }

    public static MagicStick getById(int id) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "SELECT * FROM MagicStick WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Wood wood = Wood.getById(rs.getInt("wood_id"));
                Core core = Core.getById(rs.getInt("core_id"));
                return new MagicStick(
                        rs.getInt("id"),
                        wood,
                        core,
                        rs.getDouble("price")
                );
            }
        }
        return null;
    }

    public static List<MagicStick> getAll() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<MagicStick> sticks = new ArrayList<>();
        String sql = "SELECT * FROM MagicStick ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Wood wood = Wood.getById(rs.getInt("wood_id"));
                Core core = Core.getById(rs.getInt("core_id"));
                sticks.add(new MagicStick(
                        rs.getInt("id"),
                        wood,
                        core,
                        rs.getDouble("price")
                ));
            }
        }
        return sticks;
    }

    public void delete() throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String sql = "DELETE FROM MagicStick WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    public static List<MagicStick> getUnsoldSticks() throws SQLException {
        List<MagicStick> unsoldSticks = new ArrayList<>();
        for (MagicStick stick : getAll()) {
            if (!Sale.isStickSold(stick.getId())) {
                unsoldSticks.add(stick);
            }
        }
        return unsoldSticks;
    }

    @Override
    public String toString() {
        return "MagicStick{" +
                "id=" + id +
                ", wood=" + wood.getType() +
                ", core=" + core.getType() +
                ", price=" + price +
                '}';
    }
}