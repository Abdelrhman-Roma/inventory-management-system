package main.gui;

import javax.swing.*;
import java.awt.*;
import main.service.AdminService;
import main.gui.AdminDashboard;

public class ReportFrame extends JFrame {

    JTextArea outputArea;
    AdminService adminService;

    public ReportFrame() {
        adminService = new AdminService(); 
        setTitle("Admin Reports & Offers");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel (Buttons)
        JPanel panel = new JPanel();

        JButton productBtn = new JButton("Product Report");
        JButton categoryBtn = new JButton("Category Report");
        JButton profitBtn = new JButton("Profit Report");
        JButton offerBtn = new JButton("Add Offer");

       // create back button
JButton backBtn = new JButton("Back");

// adding buttons in order cuz its imp
panel.add(backBtn);
panel.add(productBtn);
panel.add(categoryBtn);
panel.add(profitBtn);
panel.add(offerBtn);

        add(panel, BorderLayout.NORTH);

        // Center Area (Output)
        outputArea = new JTextArea();
        add(new JScrollPane(outputArea), BorderLayout.CENTER);


        backBtn.addActionListener(e -> {
          new AdminDashboard(); // go back to dashboard
          dispose();            // close current window
        });
        // Temporary actions (dummy)
        productBtn.addActionListener(e -> {
           outputArea.setText(adminService.generateProductReport());
        });

        categoryBtn.addActionListener(e -> {
    outputArea.setText(adminService.generateCategoryReport());
        });

        profitBtn.addActionListener(e -> {
    outputArea.setText(adminService.generateProfitReport());
        });

        offerBtn.addActionListener(e -> {
            String idText = JOptionPane.showInputDialog(this, "Enter Product ID:");
            if (idText == null) return;
            idText = idText.trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Product ID is required.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int productId;
            try {
                productId = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Product ID must be a valid integer.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!adminService.productExists(productId)) {
                JOptionPane.showMessageDialog(this,
                        "No product found with ID " + productId + ".",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String discountText = JOptionPane.showInputDialog(this, "Enter Discount %:");
            if (discountText == null) return;
            discountText = discountText.trim();
            if (discountText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Discount is required.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double discountValue;
            try {
                discountValue = Double.parseDouble(discountText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Discount must be a valid number.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (discountValue < 1 || discountValue > 100) {
                JOptionPane.showMessageDialog(this,
                        "Discount must be between 1 and 100.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String start = JOptionPane.showInputDialog(this, "Start Date:");
            if (start == null) return;
            start = start.trim();
            if (start.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Start date is required.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String end = JOptionPane.showInputDialog(this, "End Date:");
            if (end == null) return;
            end = end.trim();
            if (end.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "End date is required.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                adminService.addOffer(productId, discountValue, start, end);
                JOptionPane.showMessageDialog(this, "Offer Applied Successfully!");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Unable to apply offer: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}