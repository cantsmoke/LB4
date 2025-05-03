/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

public class MainFrame extends JFrame implements ActionListener {
    private JButton btnAddStick;
    private JButton btnSellStick;
    private JButton btnRestock;
    private JButton btnCheckStock;
    private JButton btnStartFresh;
    private JButton btnViewSticks;
    private JButton btnExit;
    
    public MainFrame() {
        setTitle("Магазин волшебных палочек «Олливандера»");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Создаем кнопки
        btnAddStick = new JButton("Добавить палочку");
        btnSellStick = new JButton("Продать палочку");
        btnViewSticks = new JButton("Просмотреть палочки");
        btnRestock = new JButton("Пополнить склад");
        btnCheckStock = new JButton("Проверить остатки");
        btnStartFresh = new JButton("Начать с чистого листа");
        btnExit = new JButton("Выход");

        // Подписываемся на события
        btnAddStick.addActionListener(this);
        btnSellStick.addActionListener(this);
        btnViewSticks.addActionListener(this);
        btnRestock.addActionListener(this);
        btnCheckStock.addActionListener(this);
        btnStartFresh.addActionListener(this);
        btnExit.addActionListener(this);

        // Добавляем кнопки на форму
        add(btnAddStick);
        add(btnSellStick);
        add(btnViewSticks);
        add(btnRestock);
        add(btnCheckStock);
        add(btnStartFresh);
        add(btnExit);

        setVisible(true);
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
        } else if (e.getSource() == btnExit) {
            try {
                DatabaseManager.getInstance().disconnect();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при отключении от БД: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
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
        }
    }
}