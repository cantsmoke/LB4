/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.lb4;

/**
 *
 * @author Arseniy
 */
public class LB4 {
    public static void main(String[] args) {
        // Запуск интерфейса в потоке диспетчера событий Swing
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
