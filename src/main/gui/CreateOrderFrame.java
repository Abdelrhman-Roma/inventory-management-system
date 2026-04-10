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
        // بيقفل النافذة دي بس مش البرنامج كله
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

        // تحميل الكاتيجوري من الملف
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

        // فلترة المنتجات عند اختيار كاتيجوري
        categoryCombo.addActionListener(e -> {
            String selectedCategory = (String) categoryCombo.getSelectedItem();
            productCombo.removeAllItems();
            for (Product p : Main.productService.getAllProducts()) {
                if (p.getCategory().equalsIgnoreCase(selectedCategory)) {
                    productCombo.addItem(p.getName());
                }
            }
        });

        // تصميم الواجهة
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

        // الأزرار
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

        // 1. زرار إضافة للسلة المؤقتة
        addBtn.addActionListener(e -> {
            try {
                String productName = (String) productCombo.getSelectedItem();
                if (productName == null) return;

                int qty = Integer.parseInt(quantityField.getText().trim());
                if (qty <= 0) throw new NumberFormatException();

                Product p = Main.productService.findByName(productName);
                if (p == null) return;

                cart.add(new order(0, "temp", p, qty));
                cartArea.append(String.format("• %-15s x%-3d (%s EGP)\n", p.getName(), qty, (p.getPrice() * qty)));

                double total = 0;
                for (order o : cart) total += o.getProduct().getPrice() * o.getQuantity();
                totalLabel.setText("Total: " + total + " EGP");
                quantityField.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive number!");
            }
        });

        // 2. زرار الشراء
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
                    Main.productService.reduceQuantity(o.getProduct().getName(), o.getQuantity());
                    Main.clientService.createOrder(o.getProduct().getName(), o.getQuantity());
                    anySuccess = true;
                } else {
                    failedItems.add(o);
                }
            }

            if (anySuccess) {
                Main.clientService.incrementOrderId(); 
                JOptionPane.showMessageDialog(this, "Available items purchased successfully!");
            }

            cart.clear();
            cart.addAll(failedItems);
            
            cartArea.setText("");
            double newTotal = 0;
            for (order o : cart) {
                cartArea.append("• " + o.getProduct().getName() + " x" + o.getQuantity() + " (NOT ENOUGH STOCK)\n");
                newTotal += o.getProduct().getPrice() * o.getQuantity();
            }
            totalLabel.setText("Total: " + newTotal + " EGP");

            if (!failedItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Some items are still in cart because of stock issues.");
            }
        });

        // 3. زرار الرجوع
        backBtn.addActionListener(e -> {
            this.dispose(); 
        });

        // السطر اللي طلبته لظهور الشاشة
        setVisible(true); 
    }
}