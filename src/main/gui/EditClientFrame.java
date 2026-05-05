package main.gui;

import java.awt.*;
import javax.swing.*;
import main.Main;

public class EditClientFrame extends JFrame {

    public EditClientFrame() {

        
        setTitle("Edit Profile");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input fields
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField passwordField = new JPasswordField();

        // Current data
        if (Main.clientService.getCurrentClient() != null) {
            idField.setText(String.valueOf(Main.clientService.getCurrentClient().getId()));
            idField.setEditable(false);

            nameField.setText(Main.clientService.getCurrentClient().getName());
            emailField.setText(Main.clientService.getCurrentClient().getEmail());
        }

        formPanel.add(new JLabel("Your ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("New Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("New Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Old Password:"));
        formPanel.add(oldPasswordField);

        formPanel.add(new JLabel("New Password:"));
        formPanel.add(passwordField);

        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateBtn = new JButton("Update Data");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(updateBtn);
        buttonPanel.add(backBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        updateBtn.addActionListener(e -> {
            try {
                
                int id = Integer.parseInt(idField.getText());
                String oldPassword = new String(oldPasswordField.getPassword());
                String newName = nameField.getText();
                String newEmail = emailField.getText();
                String newPassword = new String(passwordField.getPassword());

                // Check password
                if (oldPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter your old password!");
                    return;
                }

                // Update data
                boolean updated = Main.clientService.updateClientSecure(
                        id,
                        oldPassword,
                        newName,
                        newEmail,
                        newPassword
                );

                if (updated) {
                    JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong Old Password! Please try again.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please check your data.");
            }
        });

        backBtn.addActionListener(e -> dispose());

        setVisible(true);
    }
}