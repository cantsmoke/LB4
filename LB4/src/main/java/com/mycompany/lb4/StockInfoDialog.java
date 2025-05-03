/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import java.util.List;
import javax.swing.*;

class StockInfoDialog extends JDialog {
    public StockInfoDialog(JFrame parent) {
        super(parent, "Остатки на складе", true);
        setSize(500, 400);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(parent);

        try {
            List<ComponentStockInfo> stockList = ShopService.checkStock();

            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (ComponentStockInfo info : stockList) {
                listModel.addElement(info.toString());
            }

            JList<String> stockListUI = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(stockListUI);

            add(scrollPane);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);

        setVisible(true);
    }
}