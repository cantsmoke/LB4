package com.mycompany.lb4;

public class LB4 {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                DatabaseManager.getInstance().connect();
                new MainFrame();
            } catch (Exception e) {
                System.err.println("Ошибка подключения к БД: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
