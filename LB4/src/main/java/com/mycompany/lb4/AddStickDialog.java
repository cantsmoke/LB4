package com.mycompany.lb4;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AddStickDialog extends JDialog {
    private JComboBox<Wood> woodComboBox;
    private JComboBox<Core> coreComboBox;
    private JTextField priceField;

    public AddStickDialog(JFrame parent) {
        super(parent, "Добавить волшебную палочку", true);
        setSize(700, 350);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Создайте новую волшебную палочку");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 120));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel woodLabel = new JLabel("Выберите древесину:");
        woodLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(woodLabel, gbc);

        try {
            List<Wood> woods = Wood.getAll();
            woodComboBox = new JComboBox<>(woods.toArray(new Wood[0]));
            gbc.gridx = 1;
            gbc.gridy = 1;
            add(woodComboBox, gbc);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки древесины: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        JLabel coreLabel = new JLabel("Выберите сердцевину:");
        coreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(coreLabel, gbc);

        try {
            List<Core> cores = Core.getAll();
            coreComboBox = new JComboBox<>(cores.toArray(new Core[0]));
            gbc.gridx = 1;
            gbc.gridy = 2;
            add(coreComboBox, gbc);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки сердцевин: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        JLabel priceLabel = new JLabel("Цена (от 1 до 1 000 000):");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(priceLabel, gbc);

        priceField = new JTextField();
        priceField.setToolTipText("Введите цену палочки");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(priceField, gbc);

        JButton saveButton = new JButton("Создать палочку");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(46, 139, 87)); // SeaGreen
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

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

                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, "Цена должна быть больше 0.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (price > 1_000_000) {
                    JOptionPane.showMessageDialog(this, "Цена не может превышать 1 000 000.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ShopService.addNewMagicStick(selectedWood, selectedCore, price);
                JOptionPane.showMessageDialog(this, "Палочка успешно создана!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }
}