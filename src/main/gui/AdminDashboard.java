package main.gui;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton productsBtn = new JButton("Manage Products");
        JButton categoriesBtn = new JButton("Manage Categories");
        JButton suppliersBtn = new JButton("Manage Suppliers");
        JButton reportsBtn = new JButton("Reports");
        JButton logoutBtn = new JButton("Logout");

        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        JButton[] buttons = { productsBtn, categoriesBtn, suppliersBtn, reportsBtn, logoutBtn };

        for (JButton button : buttons) {
            button.setFont(buttonFont);
            button.setFocusPainted(false);
            panel.add(button);
        }

        add(panel);

        productsBtn.addActionListener(e -> {
            new ProductFrame();
            dispose();
        });

        categoriesBtn.addActionListener(e -> {
            new CategoryFrame();
            dispose();
        });

        suppliersBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Supplier management is not implemented yet."));

        reportsBtn.addActionListener(e -> {
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
