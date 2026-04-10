package main.gui;

import java.awt.*;
import javax.swing.*;
import main.Main;

public class EditClientFrame extends JFrame {

    public EditClientFrame() {

        setTitle("Edit Profile");
        setSize(400, 350); // زودنا الطول بسيط عشان الشكل يكون مريح
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 إنشاء الحقول
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField passwordField = new JPasswordField();

        // --- التعديل السحري هنا ---
        // بنجيب بيانات العميل اللي عامل Login حالياً ونحط الـ ID بتاعه فوراً
        if (Main.clientService.getCurrentClient() != null) {
            idField.setText(String.valueOf(Main.clientService.getCurrentClient().getId()));
            idField.setEditable(false); // ممنوع يعدل الـ ID بتاعه
            
            // كمان ممكن نملا الاسم والإيميل القدام عشان يسهل عليه التعديل
            nameField.setText(Main.clientService.getCurrentClient().getName());
            emailField.setText(Main.clientService.getCurrentClient().getEmail());
        }

        formPanel.add(new JLabel("Your ID (Fixed):"));
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
        JButton updateBtn = new JButton("Update Data");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(updateBtn);
        buttonPanel.add(backBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ================= ACTION =================
        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String oldPassword = new String(oldPasswordField.getPassword());
                String newName = nameField.getText();
                String newEmail = emailField.getText();
                String newPassword = new String(passwordField.getPassword());

                // بننادي الدالة المؤمنة اللي في الـ Service
                boolean updated = Main.clientService.updateClientSecure(
                        id, oldPassword, newName, newEmail, newPassword
                );

                if (updated) {
                    JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
                    dispose(); // يقفل الشاشة بعد التعديل
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