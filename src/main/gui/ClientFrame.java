package main.gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import main.Main;

public class ClientFrame extends JFrame {

    public ClientFrame() {
        setTitle("Register New Account");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Password:"));
        add(passwordField);

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        add(registerBtn);
        add(backBtn);

        registerBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            boolean success = Main.clientService.registerClient(name, email, password);

            if (success) {
                JOptionPane.showMessageDialog(this, "Registered Successfully!");
                dispose();
                new ClientDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Email already exists!");
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new ClientDashboard();
        });

        setVisible(true);
    }
}
