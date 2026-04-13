package main.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import main.Main;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel userLabel = new JLabel("Username/Email:");
        userLabel.setBounds(50, 50, 120, 30);
        add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(180, 50, 150, 30);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 30);
        add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(180, 100, 150, 30);
        add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(180, 160, 100, 30);
        add(loginBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(50, 160, 100, 30);
        add(backBtn);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            String role = RoleSelectionFrame.selectedRole;

            if (role.equals("ADMIN") && user.equals("admin") && pass.equals("1234")) {
                JOptionPane.showMessageDialog(this, "Welcome Admin");
                new AdminDashboard();
                dispose();
            } else if (role.equals("CLIENT")) {
                if (Main.clientService.loginCheck(user, pass)) {
                    JOptionPane.showMessageDialog(this, "Welcome " + user + "! Login Successful.");
                    new ClientDashboard();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "User not found in Clients.csv or Incorrect Password!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Data!");
            }
        });

        backBtn.addActionListener(e -> {
            new RoleSelectionFrame();
            dispose();
        });

        setVisible(true);
    }
}
