package com.mycompany.lb4;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

import java.awt.*;

public class MainFrame extends JFrame implements ActionListener {
    private JButton btnAddStick;
    private JButton btnSellStick;
    private JButton btnViewSticks;
    private JButton btnRestock;
    private JButton btnCheckStock;
    private JButton btnStartFresh;
    private JButton btnExit;

    public MainFrame() {
        setTitle("Магазин волшебных палочек «Олливандера»");
        setSize(700, 500);
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DatabaseManager.getInstance().disconnect();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Ошибка при отключении от БД: " + ex.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                }
                System.exit(0);
            }
        });
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Отступы между кнопками
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        btnAddStick = createStyledButton("Добавить палочку");
        btnSellStick = createStyledButton("Продать палочку");
        btnViewSticks = createStyledButton("Просмотреть палочки");
        btnRestock = createStyledButton("Пополнить склад");
        btnCheckStock = createStyledButton("Проверить остатки");
        btnStartFresh = createStyledButton("Начать с чистого листа");
        btnExit = createStyledButton("Выход");

        btnAddStick.addActionListener(this);
        btnSellStick.addActionListener(this);
        btnViewSticks.addActionListener(this);
        btnRestock.addActionListener(this);
        btnCheckStock.addActionListener(this);
        btnStartFresh.addActionListener(this);
        btnExit.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(btnAddStick, gbc);
        gbc.gridy++;
        add(btnSellStick, gbc);
        gbc.gridy++;
        add(btnViewSticks, gbc);
        gbc.gridy++;
        add(btnRestock, gbc);
        gbc.gridy++;
        add(btnCheckStock, gbc);
        gbc.gridy++;
        add(btnStartFresh, gbc);
        gbc.gridy++;
        add(btnExit, gbc);

        getContentPane().setBackground(new Color(240, 240, 255));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddStick) {
            new AddStickDialog(this);
        } else if (e.getSource() == btnSellStick) {
            new SellStickDialog(this);
        } else if (e.getSource() == btnViewSticks) {
            new StickCatalogDialog(this);
        } else if (e.getSource() == btnRestock) {
            new RestockDialog(this);
        } else if (e.getSource() == btnCheckStock) {
            new StockInfoDialog(this);
        } else if (e.getSource() == btnStartFresh) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите очистить все данные?",
                    "Подтверждение",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ShopService.startFresh();
                    JOptionPane.showMessageDialog(this, "Все данные успешно удалены.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == btnExit) {
            try {
                DatabaseManager.getInstance().disconnect();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при отключении от БД: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }
    }
}