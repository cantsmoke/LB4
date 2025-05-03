/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import java.sql.SQLException;
import java.util.List;
import javax.swing.*;

class AddStickDialog extends JDialog {
    private JComboBox<Wood> woodComboBox;
    private JComboBox<Core> coreComboBox;
    private JTextField priceField;

    public AddStickDialog(JFrame parent) {
        super(parent, "Добавить палочку", true);
        setSize(400, 300);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(parent);

        try {
            List<Wood> woods = Wood.getAll();
            List<Core> cores = Core.getAll();

            woodComboBox = new JComboBox<>(woods.toArray(new Wood[0]));
            coreComboBox = new JComboBox<>(cores.toArray(new Core[0]));

            JLabel woodLabel = new JLabel("Выберите древесину:");
            JLabel coreLabel = new JLabel("Выберите сердцевину:");
            JLabel priceLabel = new JLabel("Цена:");

            priceField = new JTextField();

            add(woodLabel);
            add(woodComboBox);
            add(coreLabel);
            add(coreComboBox);
            add(priceLabel);
            add(priceField);

            JButton saveButton = new JButton("Сохранить");
            saveButton.addActionListener(e -> {
                try {
                    Wood selectedWood = (Wood) woodComboBox.getSelectedItem();
                    Core selectedCore = (Core) coreComboBox.getSelectedItem();
                    String priceText = priceField.getText().trim();

                    if (selectedWood == null || selectedCore == null) {
                        JOptionPane.showMessageDialog(this, "Выберите древесину и сердцевину.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (priceText.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Введите цену палочки.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    double price;
                    try {
                        price = Double.parseDouble(priceText);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Цена должна быть числом.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Проверка диапазона цены
                    if (price <= 0) {
                        JOptionPane.showMessageDialog(this, "Цена должна быть больше 0.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (price > 1_000_000) {
                        JOptionPane.showMessageDialog(this, "Цена не может превышать 1 000 000.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Если всё верно — создаём палочку
                    ShopService.addNewMagicStick(selectedWood, selectedCore, price);
                    JOptionPane.showMessageDialog(this, "Палочка успешно добавлена!");
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            });

            add(saveButton);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }
}