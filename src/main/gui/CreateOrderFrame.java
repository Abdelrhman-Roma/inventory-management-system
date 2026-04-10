package main.gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import main.Main;
import main.model.Product;
import main.model.order;

public class CreateOrderFrame extends JFrame {

    private java.util.List<order> cart = new ArrayList<>();

    public CreateOrderFrame() {

        setTitle("Create Order");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Components
        JComboBox<String> categoryCombo = new JComboBox<>();
        JComboBox<String> productCombo = new JComboBox<>();
        JTextField quantityField = new JTextField();

        JTextArea cartArea = new JTextArea(6, 25);
        cartArea.setEditable(false);

        JLabel totalLabel = new JLabel("Total: 0");

        // 🔹 تحميل الكاتيجوري
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File("categories.csv"))) {
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                categoryCombo.addItem(data[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔹 فلترة المنتجات
        categoryCombo.addActionListener(e -> {
            String selectedCategory = (String) categoryCombo.getSelectedItem();
            productCombo.removeAllItems();

            for (Product p : Main.productService.getAllProducts()) {
                if (p.getCategory().equalsIgnoreCase(selectedCategory)) {
                    productCombo.addItem(p.getName());
                }
            }
        });

        // 🔹 الفورم
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryCombo);

        formPanel.add(new JLabel("Product:"));
        formPanel.add(productCombo);

        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        formPanel.add(new JLabel("Cart:"));
        formPanel.add(new JScrollPane(cartArea));

        formPanel.add(new JLabel("Total:"));
        formPanel.add(totalLabel);

        // 🔹 الأزرار
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addBtn = new JButton("Add to Cart");
        JButton createBtn = new JButton("Create Order");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(addBtn);
        buttonPanel.add(createBtn);
        buttonPanel.add(backBtn);

        // 🔹 إضافة للفريم
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================

        addBtn.addActionListener(e -> {

            String productName = (String) productCombo.getSelectedItem();
            int qty = Integer.parseInt(quantityField.getText());

            Product p = Main.productService.findByName(productName);

            if (p == null) {
                JOptionPane.showMessageDialog(this, "Product not found");
                return;
            }

            cart.add(new order(0, "temp", p, qty));

            cartArea.append(p.getName() + " x " + qty + "\n");

            double total = 0;
            for (order o : cart) {
                total += o.getProduct().getPrice() * o.getQuantity();
            }

            totalLabel.setText("Total: " + total);
        });

        createBtn.addActionListener(e -> {

    if (cart.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Cart is empty!");
        return;
    }

    for (order o : cart) {

        Main.clientService.createOrder(
                o.getProduct().getName(),
                o.getQuantity()
        );

        // 🔥 خصم الكمية
        Main.productService.reduceQuantity(
                o.getProduct().getName(),
                o.getQuantity()
        );
    }

    JOptionPane.showMessageDialog(this, "Order Created!");

    cart.clear();
    cartArea.setText("");
    totalLabel.setText("Total: 0");
});

        backBtn.addActionListener(e -> {
            new ClientDashboard();
            dispose();
        });

        setVisible(true);
    }
}