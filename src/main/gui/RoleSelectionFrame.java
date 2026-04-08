package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import main.service.ProductService;

public class RoleSelectionFrame extends JFrame {

    public static String selectedRole = "";

    public RoleSelectionFrame() {
        setTitle("Select Role");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Save data when closing
        ProductService productService = new ProductService();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Saving data...");
                productService.saveToFile();
            }
        });

        // Layout
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Choose Your Role", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Buttons Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton adminBtn = new JButton("Admin");
        JButton clientBtn = new JButton("Client");

        panel.add(adminBtn);
        panel.add(clientBtn);

        add(panel, BorderLayout.CENTER);

        // Admin Button Action
        adminBtn.addActionListener(e -> {
            selectedRole = "ADMIN";
            new LoginFrame(); 
            dispose();
        });

        // Client Button Action
        clientBtn.addActionListener(e -> {
            selectedRole = "CLIENT";
            new LoginFrame(); 
            dispose();
        });

        setVisible(true);
    }
}