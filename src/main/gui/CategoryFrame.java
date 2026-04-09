package main.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import main.service.AdminService;
import main.model.Category;

public class CategoryFrame extends JFrame {

    private JTextField idField;
    private JTextField nameField;


    private JTable table;
    private DefaultTableModel tableModel;

    private JButton backBtn;

    private AdminService service = new AdminService();

    public CategoryFrame() {

        setTitle("Category Management");
        setSize(400,400);
        setLayout(new GridLayout(5,2));

        JLabel idLabel = new JLabel("Category ID");
        idField = new JTextField();

        JLabel nameLabel = new JLabel("Category Name");
        nameField = new JTextField();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");
        backBtn = new JButton("Back");

        add(idLabel);
        add(idField);
        add(nameLabel);
        add(nameField);
        add(addBtn);
        add(updateBtn);
        add(deleteBtn);
        add(backBtn);

        // Table

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);

        // Buttons

        addBtn.addActionListener(e -> {

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();

            service.addCategory(id,name);

            refreshTable();

        });

        updateBtn.addActionListener(e -> {

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();

            service.updateCategory(id,name);

            refreshTable();

        });

        deleteBtn.addActionListener(e -> {

            int id = Integer.parseInt(idField.getText());

            service.deleteCategory(id);

            refreshTable();

        });

        backBtn.addActionListener(e -> {

            dispose();

            new AdminDashboard(); 

        });

        refreshTable();

        setVisible(true);
    }

    private void refreshTable() {

        tableModel.setRowCount(0);

        for (Category c : service.getAllCategories()) {

            Object[] row = {
                    c.getId(),
                    c.getName()
            };

            tableModel.addRow(row);

        }

    }

}