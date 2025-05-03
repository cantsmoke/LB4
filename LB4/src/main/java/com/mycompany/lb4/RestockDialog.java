/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lb4;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

class RestockDialog extends JDialog {
    private JComboBox<String> typeComboBox;
    private JTextField idField;
    private JTextField amountField;

    public RestockDialog(JFrame parent) {
        super(parent, "Пополнить запасы", true);
        setSize(400, 250);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(parent);

        typeComboBox = new JComboBox<>(new String[]{"WOOD", "CORE"});
        idField = new JTextField();
        amountField = new JTextField();

        add(new JLabel("Тип компонента (WOOD/CORE):"));
        add(typeComboBox);
        add(new JLabel("ID компонента:"));
        add(idField);
        add(new JLabel("Количество для пополнения:"));
        add(amountField);

        JButton restockButton = new JButton("Пополнить");
        restockButton.addActionListener(e -> {
            try {
                String componentType = (String) typeComboBox.getSelectedItem();
                int componentId = Integer.parseInt(idField.getText());
                int amount = Integer.parseInt(amountField.getText());

                List<ComponentRestockRequest> requests = new ArrayList<>();
                requests.add(new ComponentRestockRequest(componentType, componentId, amount));

                ShopService.restockComponents(requests);
                JOptionPane.showMessageDialog(this, "Запасы успешно пополнены!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(restockButton);
        setVisible(true);
    }
}