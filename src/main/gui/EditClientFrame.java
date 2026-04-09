
package main.gui;
import java.awt.*;
import javax.swing.*;
import main.Main;

public class EditClientFrame extends JFrame {

    public EditClientFrame() {

        setTitle("Edit Profile");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 الفورم
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField();
        JPasswordField oldPasswordField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("New Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("New Email:"));
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Old Password:"));
        formPanel.add(oldPasswordField);
        
        formPanel.add(new JLabel("New Password:"));
        formPanel.add(passwordField);

        // 🔹 الأزرار
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton updateBtn = new JButton("Update");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(updateBtn);
        buttonPanel.add(backBtn);

        // 🔹 إضافة
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);

        // ================= ACTION =================
        updateBtn.addActionListener(e -> {

            try {
                int id = Integer.parseInt(idField.getText());
                String oldPassword = new String(oldPasswordField.getPassword());
                String newName = nameField.getText();
                String newEmail = emailField.getText();
                String newPassword = new String(passwordField.getPassword());

                boolean updated = Main.clientService.updateClientSecure(
                        id, oldPassword, newName, newEmail, newPassword
                );

                if (updated) {
                    JOptionPane.showMessageDialog(this, "Updated successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong ID or password");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        backBtn.addActionListener(e -> dispose());
    }
}