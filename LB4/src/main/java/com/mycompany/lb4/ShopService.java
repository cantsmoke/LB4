package com.mycompany.lb4;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ShopService {

    public static void addNewMagicStick(Wood wood, Core core, double price) throws SQLException {
        if (wood.getAmount() <= 0 || core.getAmount() <= 0) {
            throw new IllegalStateException("Недостаточно материала для создания палочки.");
        }

        MagicStick stick = new MagicStick(0, wood, core, price);
        stick.save();

        wood.setAmount(wood.getAmount() - 1);
        wood.save();

        core.setAmount(core.getAmount() - 1);
        core.save();
    }

    public static void sellMagicStick(int stickId, String buyerName) throws SQLException {
        MagicStick stick = MagicStick.getById(stickId);
        if (stick == null) {
            throw new IllegalArgumentException("Палочка с таким ID не найдена.");
        }

        Buyer buyer = Buyer.getByName(buyerName);

        if (buyer == null) {
            buyer = new Buyer(0, buyerName);
            buyer.save();
        }

        Sale sale = new Sale(0, buyer, stick, LocalDate.now());
        sale.save();
    }

    public static void restockComponents(List<ComponentRestockRequest> requests) throws SQLException {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Список запросов не может быть пустым.");
        }

        Connection connection = DatabaseManager.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            Delivery delivery = new Delivery(0, LocalDate.now());
            delivery.save();

            for (ComponentRestockRequest request : requests) {
                String componentType = request.getComponentType();
                int componentId = request.getComponentId();
                int amount = request.getAmountToRestock();

                DeliveryDetails details = new DeliveryDetails(0, delivery.getId(), componentType, componentId, amount);
                details.save();

                if ("WOOD".equals(componentType)) {
                    Wood wood = Wood.getById(componentId);
                    wood.setAmount(wood.getAmount() + amount);
                    wood.save();
                } else if ("CORE".equals(componentType)) {
                    Core core = Core.getById(componentId);
                    core.setAmount(core.getAmount() + amount);
                    core.save();
                }
            }

            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static List<ComponentStockInfo> checkStock() throws SQLException {
        List<ComponentStockInfo> stockList = new ArrayList<>();

        for (Wood wood : Wood.getAll()) {
            stockList.add(new ComponentStockInfo(
                    "WOOD",
                    wood.getId(),
                    wood.getType(),
                    wood.getAmount()
            ));
        }

        for (Core core : Core.getAll()) {
            stockList.add(new ComponentStockInfo(
                    "CORE",
                    core.getId(),
                    core.getType(),
                    core.getAmount()
            ));
        }

        return stockList;
    }

    public static void startFresh() throws Exception {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.clearAllData();
    }
}