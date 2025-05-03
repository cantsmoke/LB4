/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;

class SellStickDialog extends JDialog {
    private JComboBox<MagicStick> stickComboBox;
    private JTextField buyerNameField;

    public SellStickDialog(JFrame parent) {
        super(parent, "Продать палочку", true);
        setSize(400, 200);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(parent);

        JLabel stickLabel = new JLabel("Выберите палочку:");
        JLabel nameLabel = new JLabel("Имя покупателя:");

        try {
            List<MagicStick> sticks = MagicStick.getUnsoldSticks();
            stickComboBox = new JComboBox<>(sticks.toArray(new MagicStick[0]));
            add(stickLabel);
            add(stickComboBox);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки палочек: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        buyerNameField = new JTextField();
        add(nameLabel);
        add(buyerNameField);

        JButton sellButton = new JButton("Продать");
        sellButton.addActionListener(e -> {
            try {
                MagicStick selectedStick = (MagicStick) stickComboBox.getSelectedItem();
                String buyerName = buyerNameField.getText();

                if (selectedStick == null || buyerName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Выберите палочку и введите имя покупателя.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ShopService.sellMagicStick(selectedStick.getId(), buyerName);
                JOptionPane.showMessageDialog(this, "Палочка успешно продана!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(sellButton);
        setVisible(true);
    }
}