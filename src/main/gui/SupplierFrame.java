package main.gui;

import main.dao.SupplierDAO;
import main.model.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SupplierFrame extends JFrame {

    JTextField idField, nameField, phoneField, addressField;
    JTable table;
    DefaultTableModel model;
    SupplierDAO dao = new SupplierDAO();

    public SupplierFrame() {

        setTitle("Supplier Management");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Form inputs
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        formPanel.add(new JLabel("Supplier ID:"));
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        idField = new JTextField(15);
        JButton autoIdBtn = new JButton("Auto-Generate");
        idPanel.add(idField);
        idPanel.add(autoIdBtn);
        formPanel.add(idPanel);

        formPanel.add(new JLabel("Supplier Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Supplier Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        add(formPanel, BorderLayout.NORTH);

        // Buttons
        JButton addBtn = new JButton("Add Supplier");
        JButton updateBtn = new JButton("Update Supplier");
        JButton deleteBtn = new JButton("Delete Supplier");
        JButton backBtn = new JButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] { "ID", "Name", "Phone", "Address" });

        table = new JTable(model);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // تحميل البيانات
        loadTable();

        //Actions
        autoIdBtn.addActionListener(e -> {
            int nextId = dao.getNextId();
            idField.setText(String.valueOf(nextId));
        });

        addBtn.addActionListener(e -> addSupplier());
        updateBtn.addActionListener(e -> updateSupplier());
        deleteBtn.addActionListener(e -> deleteSupplier());

        backBtn.addActionListener(e -> {
            new AdminDashboard().setVisible(true);
            dispose();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                idField.setText(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                phoneField.setText(model.getValueAt(row, 2).toString());
                addressField.setText(model.getValueAt(row, 3).toString());
            }
        });

        setVisible(true); // مهم جدًا
    }

    private void loadTable() {
        try {
            model.setRowCount(0);
            List<Supplier> list = dao.getAllSuppliers();

            for (Supplier s : list) {
                model.addRow(new Object[] {
                        s.getId(),
                        s.getName(),
                        s.getPhone(),
                        s.getAddress()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data");
        }
    }

    private void addSupplier() {
        try {
            String idText = idField.getText().trim();
            String nameText = nameField.getText().trim();
            String phoneText = phoneField.getText().trim();
            String addressText = addressField.getText().trim();

            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ID or click 'Auto-Generate'!");
                return;
            }

            if (nameText.isEmpty() || !nameText.matches("[A-Za-z ]+")) {
                JOptionPane.showMessageDialog(this, "Supplier name must contain only letters and spaces.");
                return;
            }

            if (phoneText.isEmpty() || !phoneText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Phone number must contain only digits.");
                return;
            }

            Supplier s = new Supplier(
                    Integer.parseInt(idText),
                    nameText,
                    phoneText,
                    addressText);

            if (dao.addSupplier(s)) {
                loadTable();
                clearFields();
                JOptionPane.showMessageDialog(this, "Added Successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Supplier ID already exists! Please use a unique ID.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID must be a numeric value.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    private void updateSupplier() {
        try {
            String idText = idField.getText().trim();
            String nameText = nameField.getText().trim();
            String phoneText = phoneField.getText().trim();
            String addressText = addressField.getText().trim();

            if (nameText.isEmpty() || !nameText.matches("[A-Za-z ]+")) {
                JOptionPane.showMessageDialog(this, "Supplier name must contain only letters and spaces.");
                return;
            }

            if (phoneText.isEmpty() || !phoneText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Phone number must contain only digits.");
                return;
            }

            Supplier s = new Supplier(
                    Integer.parseInt(idText),
                    nameText,
                    phoneText,
                    addressText);

            dao.updateSupplier(s);
            loadTable();
            clearFields();

            JOptionPane.showMessageDialog(this, "Updated Successfully");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID must be a numeric value.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating!");
        }
    }

    private void deleteSupplier() {
        try {
            int id = Integer.parseInt(idField.getText());

            dao.deleteSupplier(id);
            loadTable();
            clearFields();

            JOptionPane.showMessageDialog(this, "Deleted Successfully");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting!");
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }
}