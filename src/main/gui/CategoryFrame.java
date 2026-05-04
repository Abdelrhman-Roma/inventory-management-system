package main.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import main.service.AdminService;
import main.model.Category;
import java.util.ArrayList;

public class CategoryFrame extends JFrame {

    private JTextField idField;
    private JTextField nameField;

    private JTable table;
    private DefaultTableModel tableModel;

    private AdminService service = new AdminService();

    public CategoryFrame() {

        setTitle("Category Management");
        setSize(500,500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // top panel (input)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2,2,10,10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        idField = new JTextField();
        nameField = new JTextField();

        topPanel.add(new JLabel("Category ID"));
        topPanel.add(idField);

        topPanel.add(new JLabel("Category Name"));
        topPanel.add(nameField);

        // buttons Panel
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);

        // table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        //  add to frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // actions

        addBtn.addActionListener(e -> {

            try {

                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();

                String result = service.addCategory(id, name);

                if(result.equals("id")){
                    JOptionPane.showMessageDialog(this,"ID already exists");
                }
                else if(result.equals("name")){
                    JOptionPane.showMessageDialog(this,"Category name already exists");
                }
                else{
                    JOptionPane.showMessageDialog(this,"Added successfully");
                }

                refreshTable();

            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Invalid input");
            }
        });

        updateBtn.addActionListener(e -> {

            try {

                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();

                service.updateCategory(id, name);
                refreshTable();

            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Invalid input");
            }
        });

        deleteBtn.addActionListener(e -> {

            try {

                int id = Integer.parseInt(idField.getText());

                service.deleteCategory(id);
                refreshTable();

            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Invalid ID");
            }
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

        ArrayList<Category> list = service.getAllCategories();

        for (int i = 0; i < list.size(); i++) {
            Category c = list.get(i);
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getName()
            });
        }
    }
}