package main.gui;
import javax.swing.*;
import main.Main;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 350); // زودنا الطول بسيط عشان زرار الـ Back
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

        // إضافة زرار الـ Back بنفس ستايلك
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(50, 160, 100, 30);
        add(backBtn);

        // برمجة زرار الـ Login عشان يقرأ من الـ CSV
        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            String role = RoleSelectionFrame.selectedRole;

            // 1. دخول الـ Admin (ثابت زي ما هو)
            if (role.equals("ADMIN") && user.equals("admin") && pass.equals("1234")) {
                JOptionPane.showMessageDialog(this, "Welcome Admin");
                new AdminDashboard();
                dispose();
            } 
            // 2. دخول الـ Client (بيقرأ من الـ CSV بتاعك)
            else if (role.equals("CLIENT")) {
                if (Main.clientService.loginCheck(user, pass)) {
                    JOptionPane.showMessageDialog(this, "Welcome " + user + "! Login Successful.");
                    new ClientDashboard(); // بيفتح الـ 6 زراير
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "User not found in Clients.csv or Incorrect Password!");
                }
            } 
            else {
                JOptionPane.showMessageDialog(this, "Invalid Data!");
            }
        });

        // برمجة زرار الـ Back
        backBtn.addActionListener(e -> {
            new RoleSelectionFrame(); // يرجعك تختار الدور تاني
            dispose();
        });

        setVisible(true);
    }
}