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
            JOptionPane.showMessageDialog(this, "Offer feature coming soon...");
        });

        setVisible(true);
    }
}