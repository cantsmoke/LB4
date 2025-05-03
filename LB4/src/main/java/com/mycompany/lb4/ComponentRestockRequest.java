package com.mycompany.lb4;

public class ComponentRestockRequest {
    private String componentType;
    private int componentId;
    private int amountToRestock;

    public ComponentRestockRequest(String componentType, int componentId, int amountToRestock) {
        this.componentType = componentType;
        this.componentId = componentId;
        this.amountToRestock = amountToRestock;
    }

    public String getComponentType() {
        return componentType;
    }

    public int getComponentId() {
        return componentId;
    }

    public int getAmountToRestock() {
        return amountToRestock;
    }
}