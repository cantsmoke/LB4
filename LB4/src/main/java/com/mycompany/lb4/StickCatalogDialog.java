/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

class StickCatalogDialog extends JDialog {
    public StickCatalogDialog(JFrame parent) {
        super(parent, "Список палочек", true);
        setSize(600, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        try {
            List<MagicStick> allSticks = MagicStick.getAll();
            DefaultListModel<String> listModel = new DefaultListModel<>();

            for (MagicStick stick : allSticks) {
                String info = String.format("ID %d | Древесина: %s | Сердцевина: %s",
                        stick.getId(),
                        stick.getWood().getType(),
                        stick.getCore().getType());

                if (Sale.isStickSold(stick.getId())) {
                    List<Sale> sales = Sale.getByStick(stick);
                    if (!sales.isEmpty()) {
                        Sale sale = sales.get(0); // первая продажа
                        info += " | Куплена: " + sale.getBuyer().getName() + " (" + sale.getSaleDate() + ")";
                    }
                }

                listModel.addElement(info);
            }

            JList<String> stickList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(stickList);
            add(scrollPane, BorderLayout.CENTER);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}