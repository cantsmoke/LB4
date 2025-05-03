package com.mycompany.lb4;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class StickCatalogDialog extends JDialog {
    public StickCatalogDialog(JFrame parent) {
        super(parent, "Список всех палочек", true);
        setSize(800, 400);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(parent);

        JLabel titleLabel = new JLabel("Информация о палочках");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        try {
            JTable table = createStickTable();
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        JButton closeButton = new JButton("Закрыть");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(178, 34, 34));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTable createStickTable() throws SQLException {
        String[] columns = {"ID", "Древесина", "Сердцевина", "Статус", "Покупатель", "Дата продажи"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        List<MagicStick> allSticks = MagicStick.getAll();

        for (MagicStick stick : allSticks) {
            String woodType = stick.getWood().getType();
            String coreType = stick.getCore().getType();
            String status = Sale.isStickSold(stick.getId()) ? "Продана" : "В наличии";

            String buyerName = "";
            String saleDate = "";

            if ("Продана".equals(status)) {
                try {
                    List<Sale> sales = Sale.getByStick(stick);
                    if (!sales.isEmpty()) {
                        buyerName = sales.get(0).getBuyer().getName();
                        saleDate = sales.get(0).getSaleDate().toString();
                    }
                } catch (Exception ignored) {}
            }

            model.addRow(new Object[]{stick.getId(), woodType, coreType, status, buyerName, saleDate});
        }

        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(100, 149, 237));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setIntercellSpacing(new Dimension(10, 10));

        return table;
    }
}