/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

public class ComponentStockInfo {
    private String type; // "WOOD" или "CORE"
    private String name;
    private int currentAmount;

    public ComponentStockInfo(String type, int id, String name, int currentAmount) {
        this.type = type;
        this.name = name;
        this.currentAmount = currentAmount;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    @Override
    public String toString() {
        return type + ": " + name + " — " + currentAmount + " шт.";
    }
}