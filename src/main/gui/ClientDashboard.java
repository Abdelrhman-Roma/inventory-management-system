package main.gui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import main.Main;

public class ClientDashboard extends JFrame {

    public ClientDashboard() {
        setTitle("Client Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton loginBtn = createStyledButton("Login to My Account");
        JButton registerBtn = createStyledButton("Register New Account");
        JButton editBtn = createStyledButton("Edit Profile");
        JButton myServicesBtn = createStyledButton("My Services (Invoices & Reports)");
        JButton orderBtn = createStyledButton("Create New Order");
        JButton logoutBtn = createStyledButton("Logout");
        JButton backBtn = createStyledButton("Back");

        boolean isLoggedIn = (Main.clientService.getCurrentClient() != null);

        if (!isLoggedIn) {
            addButton(panel, loginBtn);
            addButton(panel, registerBtn);
        } else {
            addButton(panel, editBtn);
            addButton(panel, myServicesBtn);
            addButton(panel, orderBtn);
            addButton(panel, logoutBtn);
        }

        addButton(panel, backBtn);

        loginBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        registerBtn.addActionListener(e -> new ClientFrame());

        editBtn.addActionListener(e -> new EditClientFrame());

        myServicesBtn.addActionListener(e -> new ClientDashboardPart2(Main.clientService));

        orderBtn.addActionListener(e -> new CreateOrderFrame());

        logoutBtn.addActionListener(e -> {
            Main.clientService.logout();
            new ClientDashboard();
            dispose();
        });

        backBtn.addActionListener(e -> {
            new RoleSelectionFrame();
            dispose();
        });

        add(panel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return btn;
    }

    private void addButton(JPanel panel, JButton btn) {
        panel.add(btn);
        panel.add(Box.createVerticalStrut(10));
    }
}
