/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import java.sql.*;
import java.util.*;

public class ShopService {

    // Добавить новую палочку
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

    // Продать палочку
    public static void sellMagicStick(int stickId, String buyerName) throws SQLException {
        MagicStick stick = MagicStick.getById(stickId);
        if (stick == null) {
            throw new IllegalArgumentException("Палочка с таким ID не найдена.");
        }

        Buyer buyer = new Buyer(0, buyerName, stick);
        buyer.save();
    }

    // Заказать новые компоненты
    public static void restockComponents(List<ComponentRestockRequest> requests) throws SQLException {
        for (ComponentRestockRequest request : requests) {
            String componentType = request.getComponentType();
            int componentId = request.getComponentId();
            int amountToRestock = request.getAmountToRestock();

            if ("WOOD".equals(componentType)) {
                Wood wood = Wood.getById(componentId);
                wood.setAmount(wood.getAmount() + amountToRestock);
                wood.save();
            } else if ("CORE".equals(componentType)) {
                Core core = Core.getById(componentId);
                core.setAmount(core.getAmount() + amountToRestock);
                core.save();
            }
        }
    }

    // Проверить наличие на складе
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

    // Начать с чистого листа
    public static void startFresh() throws Exception {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.backupData();
        dbManager.clearAllData();
    }
}