package main.gui;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        //Buttons
        JButton productBtn = new JButton("Manage Products");
        JButton categoryBtn = new JButton("Manage Categories");
        JButton supplierBtn = new JButton("Manage Suppliers");
        JButton reportBtn = new JButton("Reports");
        JButton logoutBtn = new JButton("Logout");

        //Style
        Font font = new Font("Arial", Font.BOLD, 14);

        JButton[] buttons = {productBtn, categoryBtn, supplierBtn, reportBtn, logoutBtn};
        for (JButton btn : buttons) {
            btn.setFont(font);
            btn.setFocusPainted(false);
            panel.add(btn);
        }

        add(panel);

        //Actions
        productBtn.addActionListener(e -> {
            new ProductFrame();
            dispose();
        });

        categoryBtn.addActionListener(e -> {
            new CategoryFrame();
            dispose();
        });

        supplierBtn.addActionListener(e -> {
            new SupplierFrame();
            dispose();
        });

        reportBtn.addActionListener(e -> {
            new ReportFrame();
            dispose();
        });

        logoutBtn.addActionListener(e -> {
            new RoleSelectionFrame();
            dispose();
        });

        setVisible(true);
    }
}