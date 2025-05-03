package com.mycompany.lb4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StockInfoDialog extends JDialog {
    public StockInfoDialog(JFrame parent) {
        super(parent, "Остатки на складе", true);
        setSize(700, 400);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(parent);

        JLabel titleLabel = new JLabel("Текущие остатки материалов и сердцевин");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        try {
            JTable table = createStockTable();
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

    private JTable createStockTable() throws SQLException {
        String[] columns = {"Тип", "Название", "Количество"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        List<ComponentStockInfo> stockList = ShopService.checkStock();

        for (ComponentStockInfo info : stockList) {
            model.addRow(new Object[]{
                    info.getType(),
                    info.getName(),
                    info.getCurrentAmount()
            });
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