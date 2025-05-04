package com.mycompany.lb4;

import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SellStickDialog extends JDialog {
    private JComboBox<MagicStick> stickComboBox;
    private JTextField buyerNameField;

    public SellStickDialog(JFrame parent) {
        super(parent, "Продать палочку", true);
        setSize(800, 300);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Выберите палочку и введите имя покупателя");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(60, 60, 120));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel stickLabel = new JLabel("Выберите палочку:");
        stickLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(stickLabel, gbc);

        try {
            List<MagicStick> sticks = MagicStick.getUnsoldSticks();
            if (sticks.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Нет доступных палочек для продажи.", "Внимание", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                return;
            }

            stickComboBox = new JComboBox<>(sticks.toArray(new MagicStick[0]));
            gbc.gridx = 1;
            gbc.gridy = 1;
            add(stickComboBox, gbc);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки палочек: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JLabel nameLabel = new JLabel("Имя покупателя:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(nameLabel, gbc);

        buyerNameField = new JTextField();
        buyerNameField.setToolTipText("Введите имя покупателя");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(buyerNameField, gbc);

        JButton sellButton = new JButton("Продать палочку");
        sellButton.setFont(new Font("Arial", Font.BOLD, 14));
        sellButton.setBackground(new Color(46, 139, 87));
        sellButton.setForeground(Color.WHITE);
        sellButton.setFocusPainted(false);
        sellButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(sellButton, gbc);

        sellButton.addActionListener(e -> {
            try {
                MagicStick selectedStick = (MagicStick) stickComboBox.getSelectedItem();
                String buyerName = buyerNameField.getText().trim();

                if (selectedStick == null || buyerName.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Выберите палочку и введите имя покупателя.",
                            "Ошибка",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ShopService.sellMagicStick(selectedStick.getId(), buyerName);
                JOptionPane.showMessageDialog(this, "Палочка успешно продана!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}