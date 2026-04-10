package main.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

import main.model.Product;
import main.service.ProductService;
import main.dao.CategoryDAO;

public class ProductFrame extends JFrame {

    ProductService productService;

    JTextField idField, nameField, quantityField, priceField, productionField, expiryField;
    JComboBox<String> categoryBox; // ✅ بدل TextField
    JTable table;
    DefaultTableModel model;

    public ProductFrame() {

        productService = new ProductService();

        setTitle("Product Management");
        setSize(900,650);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addLabel("ID:", 30);
        idField = addField(150, 30);

        addLabel("Name:", 70);
        nameField = addField(150, 70);

        addLabel("Quantity:", 110);
        quantityField = addField(150, 110);

        addLabel("Price:", 150);
        priceField = addField(150, 150);

        // ✅ Category ComboBox
        addLabel("Category:", 190);
        categoryBox = new JComboBox<>();
        categoryBox.setBounds(150, 190, 150, 30);
        add(categoryBox);

        // ✅ تحميل الكاتيجوري من CSV
        CategoryDAO categoryDAO = new CategoryDAO();
        for (main.model.Category cat : categoryDAO.getAllCategories()) {
        categoryBox.addItem(cat.getName());
        }

        addLabel("Production (YYYY-MM-DD):", 230);
        productionField = addField(200, 230);

        addLabel("Expiry (YYYY-MM-DD):", 270);
        expiryField = addField(200, 270);

        // Buttons
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(400, 30, 150, 30);
        add(addBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(400, 70, 150, 30);
        add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(400, 110, 150, 30);
        add(deleteBtn);

        JButton searchNameBtn = new JButton("Search Name");
        searchNameBtn.setBounds(400, 150, 150, 30);
        add(searchNameBtn);

        JButton searchCatBtn = new JButton("Search Category");
        searchCatBtn.setBounds(400, 190, 150, 30);
        add(searchCatBtn);

        JButton searchExpiryBtn = new JButton("Search Expiry");
        searchExpiryBtn.setBounds(400, 230, 150, 30);
        add(searchExpiryBtn);

        JButton searchProdBtn = new JButton("Search Production");
        searchProdBtn.setBounds(400, 270, 150, 30);
        add(searchProdBtn);

        JButton alertBtn = new JButton("Check Alerts");
        alertBtn.setBounds(400, 310, 150, 30);
        add(alertBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(400, 350, 150, 30);
        add(refreshBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(400, 390, 150, 30);
        add(backBtn);

        // Table
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Qty");
        model.addColumn("Price");
        model.addColumn("Category");
        model.addColumn("Production");
        model.addColumn("Expiry");

        table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(30, 420, 800, 180);
        add(pane);

        // Actions

        addBtn.addActionListener(e -> {
            try {
                Product p = new Product(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        Integer.parseInt(quantityField.getText()),
                        Double.parseDouble(priceField.getText()),
                        (String) categoryBox.getSelectedItem(), // ✅
                        LocalDate.parse(productionField.getText()),
                        LocalDate.parse(expiryField.getText())
                );

                String msg = productService.addProduct(p);
                JOptionPane.showMessageDialog(this, msg);
                loadTable(productService.getAllProducts());

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Check input and date format YYYY-MM-DD");
            }
        });

        updateBtn.addActionListener(e -> {
            try {
                Product p = new Product(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        Integer.parseInt(quantityField.getText()),
                        Double.parseDouble(priceField.getText()),
                        (String) categoryBox.getSelectedItem(), // ✅
                        LocalDate.parse(productionField.getText()),
                        LocalDate.parse(expiryField.getText())
                );

                String msg = productService.updateProduct(p);
                JOptionPane.showMessageDialog(this, msg);
                loadTable(productService.getAllProducts());

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Check input and date format YYYY-MM-DD");
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                String msg = productService.deleteProduct(Integer.parseInt(idField.getText()));
                JOptionPane.showMessageDialog(this, msg);
                loadTable(productService.getAllProducts());
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID");
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        searchNameBtn.addActionListener(e -> {
            loadTable(productService.searchByName(nameField.getText()));
        });

        // ✅ البحث بالكاتيجوري من ComboBox
        searchCatBtn.addActionListener(e -> {
            loadTable(productService.searchByCategory((String) categoryBox.getSelectedItem()));
        });

        searchExpiryBtn.addActionListener(e -> {
            try {
                LocalDate d = LocalDate.parse(expiryField.getText());
                loadTable(productService.searchByExpiryDate(d));
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Wrong date format YYYY-MM-DD");
            }
        });

        searchProdBtn.addActionListener(e -> {
            try {
                LocalDate d = LocalDate.parse(productionField.getText());
                loadTable(productService.searchByProductionDate(d));
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Wrong date format YYYY-MM-DD");
            }
        });

        alertBtn.addActionListener(e -> {

            List<Product> low = productService.checkLowStock();
            List<Product> exp = productService.checkExpiry();

            String msg = "";

            for(Product p : low) {
                msg += "Low Stock: " + p.getName() + " (Qty=" + p.getQuantity() + ")\n";
            }

            for(Product p : exp) {
                msg += "Expiring Soon: " + p.getName() + "\n";
            }

            if(msg.isEmpty()) {
                msg = "No Alerts";
            }

            JOptionPane.showMessageDialog(this, msg);
        });

        refreshBtn.addActionListener(e -> loadTable(productService.getAllProducts()));

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();

                idField.setText(model.getValueAt(row,0).toString());
                nameField.setText(model.getValueAt(row,1).toString());
                quantityField.setText(model.getValueAt(row,2).toString());
                priceField.setText(model.getValueAt(row,3).toString());
                categoryBox.setSelectedItem(model.getValueAt(row,4).toString()); // ✅
                productionField.setText(model.getValueAt(row,5).toString());
                expiryField.setText(model.getValueAt(row,6).toString());
            }
        });

        setVisible(true);
        loadTable(productService.getAllProducts());
    }

    private void addLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(30, y, 180, 30);
        add(label);
    }

    private JTextField addField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 150, 30);
        add(field);
        return field;
    }

    private void loadTable(List<Product> list) {
        model.setRowCount(0);

        for(Product p : list) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getQuantity(),
                    p.getPrice(),
                    p.getCategory(),
                    p.getProductionDate(),
                    p.getExpiryDate()
            });
        }
    }
}