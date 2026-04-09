package main.gui;

import java.awt.*;
import javax.swing.*;

public class ClientDashboard extends JFrame {

    public ClientDashboard() {
        setTitle("Client Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        JButton registerBtn = new JButton("Register");
        JButton editBtn = new JButton("Edit Profile");
        JButton orderBtn = new JButton("Create Order");
        JButton backBtn = new JButton("Back");
        panel.add(registerBtn);
        panel.add(editBtn);
        panel.add(orderBtn);
        panel.add(backBtn);

        registerBtn.addActionListener(e -> {
    new ClientFrame();
    });
        editBtn.addActionListener(e -> {
    new EditClientFrame();
    });
        orderBtn.addActionListener(e -> {
    new CreateOrderFrame();
    });
        backBtn.addActionListener(e -> {
    new RoleSelectionFrame();
    dispose();
    });
    add(panel);
        setVisible(true);
    }
}