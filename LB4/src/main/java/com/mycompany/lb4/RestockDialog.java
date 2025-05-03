package com.mycompany.lb4;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.sql.SQLException;

public class RestockDialog extends JDialog {
    private JComboBox<String> existingTypeComboBox;
    //private JTextField customTypeField;
    private JComboBox<String> componentTypeCombo;
    private JTextField amountField;
    private DefaultTableModel tableModel;
    private List<ComponentRestockRequest> requests = new ArrayList<>();

    public RestockDialog(JFrame parent) {
        super(parent, "Пополнение склада", true);
        setSize(1000, 500);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(parent);

        JLabel titleLabel = new JLabel("Заказ новых компонентов");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        componentTypeCombo = new JComboBox<>(new String[]{"WOOD", "CORE"});
        formPanel.add(new JLabel("Тип компонента (WOOD / CORE):"));
        formPanel.add(componentTypeCombo);

        existingTypeComboBox = new JComboBox<>();
        formPanel.add(new JLabel("Существующий тип:"));
        formPanel.add(existingTypeComboBox);
        
        componentTypeCombo.addActionListener(e -> {
            String selectedType = (String) componentTypeCombo.getSelectedItem();
            updateExistingTypes(selectedType);
        });

        // Поле ввода нового типа
        //customTypeField = new JTextField();
        //customTypeField.setToolTipText("Или введите новый тип");
        //formPanel.add(new JLabel("Новый тип (если нет в списке):"));
        //formPanel.add(customTypeField); // Исправление: используем customTypeField вместо customTypeComboBox

        amountField = new JTextField();
        formPanel.add(new JLabel("Количество:"));
        formPanel.add(amountField);

        JButton addButton = new JButton("Добавить компонент");
        addButton.setBackground(new Color(65, 105, 225));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> addComponentToTable());
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        add(formPanel, BorderLayout.EAST);

        String[] columns = {"Тип компонента", "Название", "Количество"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable requestTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(requestTable);
        add(scrollPane, BorderLayout.WEST);

        JButton createButton = new JButton("Создать поставку");
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        createButton.setBackground(new Color(46, 139, 87));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        createButton.addActionListener(e -> {
            if (requests.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Добавьте хотя бы один компонент.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                ShopService.restockComponents(requests);
                JOptionPane.showMessageDialog(this, "Поставка успешно создана!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании поставки: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Закрыть");
        cancelButton.setBackground(new Color(178, 34, 34));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addComponentToTable() {
        String componentType = (String) componentTypeCombo.getSelectedItem();
        String selectedExisting = (String) existingTypeComboBox.getSelectedItem();
        //String customType = customTypeField.getText().trim();
        String amountText = amountField.getText().trim();
        
        if (selectedExisting == null) {
            JOptionPane.showMessageDialog(this, "Выберите название компонента.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите количество.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Количество должно быть положительным числом.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fullTypeName = selectedExisting;
        //String fullTypeName = (selectedExisting != null ? selectedExisting : customType).trim();
        if (fullTypeName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Выберите название компонента.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean exists = false;
        int existingId = -1;

        try {
            List<?> components = "WOOD".equals(componentType) ? Wood.getAll() : Core.getAll();
            for (Object obj : components) {
                String typeName = "";
                if (obj instanceof Wood) typeName = ((Wood) obj).getType().toLowerCase();
                if (obj instanceof Core) typeName = ((Core) obj).getType().toLowerCase();

                if (typeName.equals(fullTypeName.toLowerCase())) {
                    exists = true;
                    if (obj instanceof Wood) existingId = ((Wood) obj).getId();
                    if (obj instanceof Core) existingId = ((Core) obj).getId();
                    break;
                }
            }
        } catch (SQLException ignored) {}

        tableModel.addRow(new Object[]{componentType, fullTypeName, amount});

        requests.add(new ComponentRestockRequest(componentType, existingId, amount));

        //customTypeField.setText("");
        amountField.setText("");
    }
    
    private void updateExistingTypes(String componentType) {
        try {
            List<?> components = "WOOD".equals(componentType) ? Wood.getAll() : Core.getAll();
            Set<String> typeNames = new HashSet<>();

            for (Object obj : components) {
                if (obj instanceof Wood) {
                    typeNames.add(((Wood) obj).getType().toLowerCase());
                } else if (obj instanceof Core) {
                    typeNames.add(((Core) obj).getType().toLowerCase());
                }
            }

            String[] types = typeNames.toArray(new String[0]);
            existingTypeComboBox.setModel(new DefaultComboBoxModel<>(types));
            existingTypeComboBox.setSelectedItem(null);
            //customTypeField.setText("");
            amountField.setText("");
        } catch (SQLException ex) {
            existingTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[0]));
            JOptionPane.showMessageDialog(this, "Ошибка загрузки типов: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}