package main.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.service.ProductService;

public class RoleSelectionFrame extends JFrame {

    public static String selectedRole = "";

    public RoleSelectionFrame() {
        setTitle("Select Role");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ProductService productService = new ProductService();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closing...");
            }
        });

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Choose Your Role", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton adminBtn = new JButton("Admin");
        JButton clientBtn = new JButton("Client");

        panel.add(adminBtn);
        panel.add(clientBtn);

        add(panel, BorderLayout.CENTER);

        adminBtn.addActionListener(e -> {
            selectedRole = "ADMIN";
            new LoginFrame();
            dispose();
        });

        clientBtn.addActionListener(e -> {
            selectedRole = "CLIENT";
            new ClientDashboard();
            dispose();
        });

        setVisible(true);
    }
}
