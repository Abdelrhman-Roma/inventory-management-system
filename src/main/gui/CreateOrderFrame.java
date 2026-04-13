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
        setTitle("Create Order - Client Side");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLayout(new BorderLayout());

        // --- المكونات (UI Components) ---
        JComboBox<String> categoryCombo = new JComboBox<>();
        JComboBox<String> productCombo = new JComboBox<>();
        JTextField quantityField = new JTextField();

        JTextArea cartArea = new JTextArea(10, 30);
        cartArea.setEditable(false);
        cartArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        cartArea.setBorder(BorderFactory.createTitledBorder("Current Cart"));

        JLabel totalLabel = new JLabel("Total: 0 EGP");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 15));
        totalLabel.setForeground(new Color(0, 102, 0));

        // تحميل الكاتيجوري
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File("categories.csv"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if(line.isEmpty()) continue;
                String[] data = line.split(",");
                if(data.length > 1) categoryCombo.addItem(data[1]);
            }
        } catch (Exception e) {
            System.out.println("Categories file error.");
        }

        categoryCombo.addActionListener(e -> {
            String selectedCategory = (String) categoryCombo.getSelectedItem();
            productCombo.removeAllItems();
            for (Product p : Main.productService.getAllProducts()) {
                if (p.getCategory().equalsIgnoreCase(selectedCategory)) {
                    productCombo.addItem(p.getName());
                }
            }
        });

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryCombo);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productCombo);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(cartArea), BorderLayout.CENTER);
        
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.add(totalLabel);
        centerPanel.add(summaryPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add to Cart");
        JButton createBtn = new JButton("Confirm & Buy");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(addBtn);
        buttonPanel.add(createBtn);
        buttonPanel.add(backBtn);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ================= الأكشن (Actions) =================

        // 1. زرار إضافة للسلة
       addBtn.addActionListener(e -> {
    try {
        String productName = (String) productCombo.getSelectedItem();
        if (productName == null) return;

        int qty = Integer.parseInt(quantityField.getText().trim());
        if (qty <= 0) throw new NumberFormatException();

        Product p = Main.productService.findByName(productName);
        if (p == null) return;

        // ✅ check الكمية قبل الإضافة
        int alreadyInCart = 0;

        for (order o : cart) {
         if (o.getProduct().getName().equalsIgnoreCase(productName)) {
        alreadyInCart += o.getQuantity();
         }
        }
        int available = p.getQuantity() - alreadyInCart;

        if (qty > available) {
            JOptionPane.showMessageDialog(this,
        "Not enough stock! Only " + available + " items available.");
            return;
            }

        String currentUserName = Main.clientService.getCurrentClient().getName();

        order newTempOrder = new order(0, currentUserName, p, qty); 
        cart.add(newTempOrder);

        cartArea.append(String.format("• %-15s x%-3d (%s EGP)\n", p.getName(), qty, (p.getPrice() * qty)));

        double total = 0;
        for (order o : cart) total += o.getTotalPrice();
        totalLabel.setText("Total: " + total + " EGP");

        quantityField.setText("");

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter a valid positive number!");
    }
});
        // 2. زرار الشراء النهائي
        createBtn.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!");
                return;
            }

            ArrayList<order> failedItems = new ArrayList<>();
            boolean anySuccess = false;

            for (order o : cart) {
                Product realProduct = Main.productService.findByName(o.getProduct().getName());
                
                if (realProduct != null && realProduct.getQuantity() >= o.getQuantity()) {
                    // تقليل الكمية من المخزن
                    Main.productService.reduceQuantity(o.getProduct().getName(), o.getQuantity());
                    // تسجيل الأوردر في الـ CSV والرامات
                    Main.clientService.createOrder(o.getProduct().getName(), o.getQuantity());
                    anySuccess = true;
                } else {
                    failedItems.add(o);
                }
            }

            if (anySuccess) {
                // شلنا السطر اللي كان عامل خطأ أحمر (incrementOrderId)
                JOptionPane.showMessageDialog(this, "Available items purchased successfully!");
            }

            cart.clear();
            if (!failedItems.isEmpty()) {
                cart.addAll(failedItems);
                cartArea.setText("--- OUT OF STOCK ---\n");
                for (order o : failedItems) {
                    cartArea.append("• " + o.getProduct().getName() + " x" + o.getQuantity() + "\n");
                }
            } else {
                cartArea.setText("");
                totalLabel.setText("Total: 0 EGP");
            }

            if (!failedItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Some items failed due to stock issues.");
            }
        });

        backBtn.addActionListener(e -> this.dispose());

        setVisible(true); 
    }
}